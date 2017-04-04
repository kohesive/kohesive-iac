package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.emitters.GeneratorTaskExecutor
import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.model.intermediate.ShapeModel
import freemarker.template.Template
import uy.kohesive.iac.model.aws.codegen.TemplateDescriptor
import java.io.StringWriter
import java.io.Writer

//data class RequestMapNode(
//    val
//)

val DEBUG = HashSet<String>()

class AWSApiCallBuilder(
    val awsModel: IntermediateModel,
    val event: CloudTrailEvent
) {

    private fun doStuff(map: Map<String, Any>, shapeModel: ShapeModel, awsModel: IntermediateModel) {
        val some = shapeModel.membersAsMap.keys.map(String::toLowerCase).toSet() + shapeModel.members?.map { it.http?.unmarshallLocationName?.orEmpty() }?.filterNotNull()?.map(String::toLowerCase)?.orEmpty()?.toSet().orEmpty()

        map.forEach { fieldName, fieldValue ->
            if (!some.contains(fieldName.toLowerCase())) {
                DEBUG.add("Shape ${shapeModel.shapeName} doesn't have member $fieldName")
            }
        }
    }

    fun build(): String {
        val generatorTaskExecutor = GeneratorTaskExecutor()

        val apiCallData = ApiCallData(
            shape      = awsModel.shapes[event.eventName + "Request"] ?: throw IllegalStateException("Can't find a shape for event $event"),
            requestMap = event.request.orEmpty()
        )

        val op = awsModel.getOperation(event.eventName)

        // TODO?
        doStuff(apiCallData.requestMap, apiCallData.shape, awsModel)

        val stringWriter = StringWriter()
//        val emitter      = CodeEmitter(listOf(GenerateApiCallsTask.create(stringWriter, apiCallData)), generatorTaskExecutor)
//        emitter.emit()
//
//        generatorTaskExecutor.waitForCompletion()
//        generatorTaskExecutor.shutdown()

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