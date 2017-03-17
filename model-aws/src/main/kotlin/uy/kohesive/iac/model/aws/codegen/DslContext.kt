package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.emitters.CodeWriter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.emitters.GeneratorTaskParams
import com.amazonaws.codegen.model.intermediate.IntermediateModel
import freemarker.template.Template
import java.io.Writer

class DslContextGeneratorTask private constructor(writer: Writer, template: Template, data: ContextData)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(taskParams: GeneratorTaskParams, model: IntermediateModel): DslContextGeneratorTask {
            val contextData = ContextData(model)
            return DslContextGeneratorTask(
                CodeWriter(
                    taskParams.pathProvider.outputDirectory + "/" + ContextData.PackagePath,
                    contextData.serviceName,
                    ".kt"
                ),
                TemplateDescriptor.Context.load(),
                contextData
            )
        }
    }

}

data class ContextData(val model: IntermediateModel) {

    companion object {
        val PackageName = "uy.kohesive.iac.model.aws.contexts.generated"
        val PackagePath = PackageName.replace('.', '/')
    }

    val contextPackageName = ContextData.PackageName

    val metadata      = model.metadata
    val serviceName   = model.getShortServiceName()
    val serviceNameLC = serviceName.take(1).toLowerCase() + serviceName.drop(1)
    val syncInterface = model.metadata.syncInterface

}
