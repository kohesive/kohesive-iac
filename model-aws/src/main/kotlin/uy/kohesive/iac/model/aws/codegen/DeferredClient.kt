package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.emitters.CodeWriter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import freemarker.template.Template
import java.io.File
import java.io.Writer


class DeferredClientGeneratorTask private constructor(writer: Writer, template: Template, data: DeferredClientData)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(params: KohesiveGenerateParams, baseContextData: BaseContextData): DeferredClientGeneratorTask {
            val versionPostfix = VersionUtil.getExplicitServiceNameVersion(params.model.metadata.serviceName)?.toUpperCase() ?: ""
            val clientData     = DeferredClientData(params, versionPostfix)

            val contextInfo = GeneratedClientInfo(
                versioned               = versionPostfix.isNotEmpty(),
                clientFieldName         = ContextData.getClientFieldName(params.model, versionPostfix),
                deferredClientClassName = clientData.deferredClientClassName,
                awsInterfaceClassName   = clientData.syncInterface,
                awsInterfaceClassFq     = params.model.metadata.packageName + "." + params.model.metadata.syncInterface
            )
            baseContextData.clients.add(contextInfo)

            return DeferredClientGeneratorTask(
                CodeWriter(
                    params.taskParams.pathProvider.outputDirectory + "/" + DeferredClientData.PackagePath,
                    contextInfo.deferredClientClassName,
                    ".kt"
                ),
                TemplateDescriptor.DeferredClient.load(),
                clientData
            )
        }
    }

}

data class DeferredClientData(val params: KohesiveGenerateParams, val versionPostfix: String = "") {

    companion object {
        val PackageName = "uy.kohesive.iac.model.aws.clients"
        val PackagePath = PackageName.replace('.', '/')
    }

    val targetClientPackageName = DeferredClientData.PackageName
    val awsClientPackageName    = params.model.metadata.packageName

    val metadata      = params.model.metadata
    val serviceName   = params.model.getShortServiceName()
    val serviceNameLC = serviceName.take(1).toLowerCase() + serviceName.drop(1)
    val syncInterface = params.model.metadata.syncInterface

    val deferredClientClassName     = "Deferred" + syncInterface + versionPostfix
    val baseDeferredClientClassName = "BaseDeferred" + syncInterface + versionPostfix

    val generateSubClient: Boolean
        = !File(params.mainSourcesDir, "$PackagePath/$deferredClientClassName.kt").exists()

}
