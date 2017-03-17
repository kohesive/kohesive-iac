package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.emitters.CodeEmitter
import com.amazonaws.codegen.emitters.GeneratorTaskExecutor
import com.amazonaws.codegen.emitters.GeneratorTaskParams
import com.amazonaws.codegen.emitters.tasks.BaseGeneratorTasks
import com.amazonaws.codegen.internal.Jackson
import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.utils.ModelLoaderUtils
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import java.io.InputStream
import java.util.regex.Pattern

fun main(args: Array<String>) {
    KohesiveAwsCodeGen("/Users/eliseyev/TMP/codegen/").generate()
}

data class IntermediateFile(
    val filePath: String,
    val serviceKind: String,
    val date: String
)

class KohesiveAwsCodeGen(val outputDirectory: String) {

    fun generate() {
        val intermediateFiles = Reflections("models", ResourcesScanner()).getResources(Pattern.compile(".*intermediate\\.json"))

        val recentIntermediateFiles = intermediateFiles.map { filePath ->
            val filename = filePath.substring(filePath.lastIndexOf('/')).drop(1)
            IntermediateFile(
                filePath    = filePath,
                serviceKind = filename.substring(0, filename.indexOf('-')),
                date        = filename.substring(filename.indexOf('-'), filename.lastIndexOf('-')).drop(1)
            )
        }.groupBy { it.serviceKind }.mapValues {
            it.value.maxBy { it.date }
        }.values.filterNotNull().asSequence()

        recentIntermediateFiles.map { it.filePath }.forEach { modelFile ->
            ModelLoaderUtils.getRequiredResourceAsStream(modelFile).use { stream ->
                val intermediateModel: IntermediateModel = loadModel(stream)

                val params = GeneratorTaskParams.create(intermediateModel, outputDirectory, outputDirectory)

                val kohesiveGeneratorTasks = KohesiveGeneratorTasks(params)
                val emitter = CodeEmitter(kohesiveGeneratorTasks, GeneratorTaskExecutor())
                emitter.emit()
            }
        }
    }

}

fun IntermediateModel.getShortServiceName() = metadata.syncInterface.replace("Amazon", "").replace("AWS", "")

class KohesiveGeneratorTasks(val params: GeneratorTaskParams) : BaseGeneratorTasks(params) {

    private val baseDirectory = params.pathProvider.outputDirectory

    override fun createTasks() = listOf(
        DslContextGeneratorTask.create(params, model),
        DeferredClientGeneratorTask.create(params, model)
    )

}

inline fun <reified T> loadModel(inputStream: InputStream): T {
    return Jackson.load(T::class.java, inputStream)
}