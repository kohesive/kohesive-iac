package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.emitters.CodeEmitter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.emitters.GeneratorTaskExecutor
import com.amazonaws.codegen.emitters.GeneratorTaskParams
import com.amazonaws.codegen.emitters.tasks.BaseGeneratorTasks
import com.amazonaws.codegen.internal.Jackson
import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.utils.ModelLoaderUtils
import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val stream = ModelLoaderUtils.getRequiredResourceAsStream("models/dynamodb-2012-08-10-intermediate.json")
    val intermediateModel: IntermediateModel = loadModel(stream)

    val outputDirectory = File("/Users/eliseyev/TMP/codegen/").path
    val params = GeneratorTaskParams.create(intermediateModel, outputDirectory, outputDirectory)

    val templateLoader = TemplateLoader()

    val kohesiveGeneratorTasks = KohesiveGeneratorTasks(params, templateLoader)
    val emitter = CodeEmitter(kohesiveGeneratorTasks, GeneratorTaskExecutor())
    emitter.emit()

    System.exit(0)
}

data class ContextData(val model: IntermediateModel) {

    companion object {
        val PackageName = "uy.kohesive.iac.model.aws.contexts"
        val PackagePath = PackageName.replace('.', '/')
    }

    val contextPackageName = ContextData.PackageName

    val metadata      = model.metadata
    val serviceName   = model.metadata.serviceAbbreviation
    val serviceNameLC = serviceName.take(1).toLowerCase() + serviceName.drop(1)
    val syncInterface = model.metadata.syncInterface
}

class KohesiveGeneratorTasks(params: GeneratorTaskParams, val templateLoader: TemplateLoader) : BaseGeneratorTasks(params) {

    private val baseDirectory = params.pathProvider.outputDirectory

    override fun createTasks() = listOf(
        createServiceDSLContext()
    )

    private fun createServiceDSLContext() = FreemarkerGeneratorTask(
        baseDirectory + "/" + ContextData.PackagePath,
        model.metadata.serviceAbbreviation + ".kt",
        templateLoader.getTemplate("/templates/context/Context.ftl"),
        ContextData(model)
    )

}

inline fun <reified T> loadModel(inputStream: InputStream): T {
    return Jackson.load(T::class.java, inputStream)
}