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
        fun create(taskParams: GeneratorTaskParams, model: IntermediateModel, baseContextData: BaseContextData): DslContextGeneratorTask {
            val contextData = ContextData(model)

            baseContextData.contexts.add(GeneratedContextInfo(
                enabledClassName = "${contextData.serviceName}Enabled",
                contextClassName = "${contextData.serviceName}Context",
                contextFieldName = "${contextData.serviceNameLC}Context"
            ))

            return DslContextGeneratorTask(
                CodeWriter(
                    taskParams.pathProvider.outputDirectory + "/" + ContextData.PackagePath,
                    contextData.serviceName,
                    ".kt"
                ),
                TemplateDescriptor.ServiceContext.load(),
                contextData
            )
        }
    }

}

data class ContextData(val model: IntermediateModel) {

    companion object {
        val PackageName = "uy.kohesive.iac.model.aws.contexts"
        val PackagePath = PackageName.replace('.', '/')

        fun getServiceName(model: IntermediateModel)     = model.getShortServiceName()
        fun getClientFieldName(model: IntermediateModel) = (model.getShortServiceName().let {
            if (it.all { it.isUpperCase() || it.isDigit() }) {
                it.toLowerCase()
            } else {
                it
            }
        }).let { shortServiceNameLC ->
            shortServiceNameLC.take(1).toLowerCase() + shortServiceNameLC.drop(1) + "Client"
        }
    }

    val contextPackageName = ContextData.PackageName

    val metadata      = model.metadata

    val serviceName   = getServiceName(model)
    val serviceNameLC = getClientFieldName(model)
    val syncInterface = model.metadata.syncInterface

}
