package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.emitters.CodeWriter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.emitters.GeneratorTaskParams
import com.amazonaws.codegen.model.intermediate.IntermediateModel
import freemarker.template.Template
import java.io.Writer

class DeferredClientGeneratorTask private constructor(writer: Writer, template: Template, data: DeferredClientData)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(taskParams: GeneratorTaskParams, model: IntermediateModel): DeferredClientGeneratorTask {
            val clientData = DeferredClientData(model)
            return DeferredClientGeneratorTask(
                CodeWriter(
                    taskParams.pathProvider.outputDirectory + "/" + DeferredClientData.PackagePath,
                    "Deferred" + clientData.syncInterface,
                    ".kt"
                ),
                TemplateDescriptor.DeferredClient.load(),
                clientData
            )
        }
    }

}

data class DeferredClientData(val model: IntermediateModel) {

    companion object {
        val PackageName = "uy.kohesive.iac.model.aws.clients.generated"
        val PackagePath = PackageName.replace('.', '/')
    }

    val targetClientPackageName = DeferredClientData.PackageName
    val awsClientPackageName = model.metadata.packageName

    val metadata      = model.metadata
    val serviceName   = model.getShortServiceName()
    val serviceNameLC = serviceName.take(1).toLowerCase() + serviceName.drop(1)
    val syncInterface = model.metadata.syncInterface

}
