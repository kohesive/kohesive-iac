package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.emitters.*
import com.amazonaws.codegen.emitters.tasks.BaseGeneratorTasks
import com.amazonaws.codegen.internal.Jackson
import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.utils.ModelLoaderUtils
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import java.io.File
import java.io.InputStream
import java.util.regex.Pattern


data class IntermediateFile(
    val filePath: String,
    val serviceKind: String,
    val date: String
)

fun main(args: Array<String>) {
    val intermediateFiles = Reflections("models", ResourcesScanner()).getResources(Pattern.compile(".*intermediate\\.json"))

    val recentIntermediateFiles = intermediateFiles.map { filePath ->
        val filename    = filePath.substring(filePath.lastIndexOf('/')).drop(1)
        IntermediateFile(
            filePath    = filePath,
            serviceKind = filename.substring(0, filename.indexOf('-')),
            date        = filename.substring(filename.indexOf('-'), filename.lastIndexOf('-')).drop(1)
        )
    }.groupBy { it.serviceKind }.mapValues {
        it.value.maxBy { it.date }
    }.values.filterNotNull()

    recentIntermediateFiles.map { it.filePath }.forEach { modelFile ->
        ModelLoaderUtils.getRequiredResourceAsStream(modelFile).use { stream ->
            val intermediateModel: IntermediateModel = loadModel(stream)

            val outputDirectory = File("/Users/eliseyev/TMP/codegen/").path
            val params = GeneratorTaskParams.create(intermediateModel, outputDirectory, outputDirectory)

            val templateLoader = TemplateLoader()

            val kohesiveGeneratorTasks = KohesiveGeneratorTasks(params, templateLoader)
            val emitter = CodeEmitter(kohesiveGeneratorTasks, GeneratorTaskExecutor())
            emitter.emit()
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
    val serviceName   = model.metadata.syncInterface.replace("Amazon", "").replace("AWS", "")
    val serviceNameLC = serviceName.take(1).toLowerCase() + serviceName.drop(1)
    val syncInterface = model.metadata.syncInterface
}

class KohesiveGeneratorTasks(params: GeneratorTaskParams, val templateLoader: TemplateLoader) : BaseGeneratorTasks(params) {

    private val baseDirectory = params.pathProvider.outputDirectory

    override fun createTasks() = listOf(
            createServiceDSLContext()
    )

    private fun createServiceDSLContext() = ContextData(model).let { contextData ->
        FreemarkerGeneratorTask(
            CodeWriter(
                baseDirectory + "/" + ContextData.PackagePath,
                contextData.serviceName,
                ".kt"
            ),
            templateLoader.getTemplate("/templates/context/Context.ftl"),
            contextData
        )
    }

}

inline fun <reified T> loadModel(inputStream: InputStream): T {
    return Jackson.load(T::class.java, inputStream)
}