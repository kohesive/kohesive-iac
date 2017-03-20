package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.emitters.CodeWriter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.model.intermediate.IntermediateModel
import freemarker.template.Template
import java.io.File
import java.io.Writer

class ServiceContextGeneratorTask private constructor(writer: Writer, template: Template, data: ContextData)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(params: KohesiveGenerateParams, baseContextData: BaseContextData): ServiceContextGeneratorTask {
            val versionPostfix = VersionUtil.getExplicitServiceNameVersion(params.model.metadata.serviceName)?.toUpperCase() ?: ""
            val contextData    = ContextData(params, versionPostfix)

            baseContextData.contexts.add(GeneratedContextInfo(
                enabledClassName = "${contextData.serviceName}Enabled",
                contextClassName = "${contextData.serviceName}Context",
                contextFieldName = "${contextData.serviceNameLC}Context"
            ))

            return ServiceContextGeneratorTask(
                CodeWriter(
                    params.taskParams.pathProvider.outputDirectory + "/" + ContextData.PackagePath,
                    contextData.serviceName,
                    ".kt"
                ),
                TemplateDescriptor.ServiceContext.load(),
                contextData
            )
        }
    }

}

data class ContextData(val params: KohesiveGenerateParams, val versionPostfix: String = "") {

    companion object {
        val PackageName = "uy.kohesive.iac.model.aws.contexts"
        val PackagePath = PackageName.replace('.', '/')

        fun getServiceName(model: IntermediateModel, versionPostfix: String) = model.getShortServiceName() + versionPostfix

        fun getClientFieldName(model: IntermediateModel, versionPostfix: String)
            = getServiceNameLC(model, versionPostfix) + "Client"

        private fun getServiceNameLC(model: IntermediateModel, versionPostfix: String): String {
            return (model.getShortServiceName().let {
                if (it.all { it.isUpperCase() || it.isDigit() }) {
                    it.toLowerCase()
                } else {
                    it
                }
            }).let { shortServiceNameLC ->
                shortServiceNameLC.take(1).toLowerCase() + shortServiceNameLC.drop(1)
            } + versionPostfix
        }
    }

    val contextPackageName = ContextData.PackageName

    val metadata      = params.model.metadata

    val serviceName   = getServiceName(params.model, versionPostfix)
    val serviceNameLC = getServiceNameLC(params.model, versionPostfix)
    val syncInterface = params.model.metadata.syncInterface

    val generateSubContext: Boolean
        = !File(params.mainSourcesDir, "$PackagePath/$serviceName.kt").exists()

}
