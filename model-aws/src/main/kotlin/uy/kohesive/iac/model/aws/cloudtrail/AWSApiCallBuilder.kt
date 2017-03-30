package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.codegen.emitters.CodeEmitter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.emitters.GeneratorTaskExecutor
import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.model.intermediate.ShapeModel
import freemarker.template.Template
import uy.kohesive.iac.model.aws.codegen.TemplateDescriptor
import java.io.StringWriter
import java.io.Writer

class AWSApiCallBuilder(
    val awsModel: IntermediateModel,
    val event: CloudTrailEvent
) {

    fun build(): String {
        val generatorTaskExecutor = GeneratorTaskExecutor()

        val apiCallData = ApiCallData(
            shape      = awsModel.shapes[event.eventName + "Request"] ?: throw IllegalStateException("Can't find a shape for event $event"),
            requestMap = event.request.orEmpty()
        )

        val stringWriter = StringWriter()
        val emitter      = CodeEmitter(listOf(GenerateApiCallsTask.create(stringWriter, apiCallData)), generatorTaskExecutor)
        emitter.emit()

        generatorTaskExecutor.waitForCompletion()
        generatorTaskExecutor.shutdown()

        return stringWriter.buffer.toString()
    }

}

data class ApiCallData(
    val shape: ShapeModel,
    val requestMap: Map<String, Any>
)

class GenerateApiCallsTask private constructor(writer: Writer, template: Template, data: Any)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(stringWriter: StringWriter, data: ApiCallData): GenerateApiCallsTask {
            return GenerateApiCallsTask(
                stringWriter,
                TemplateDescriptor.RequestBuilder.load(),
                data
            )
        }
    }

}