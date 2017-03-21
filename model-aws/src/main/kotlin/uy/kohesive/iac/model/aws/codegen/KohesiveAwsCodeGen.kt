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
import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    KohesiveAwsCodeGen(
        outputDirectory     = "/Users/eliseyev/TMP/codegen/",
        mainSourceDirectory = "/Users/eliseyev/git/kohesive-iac/model-aws/src/main/kotlin/"
    ).generate()
}

data class KohesiveGenerateParams(
    val taskParams: GeneratorTaskParams,
    val mainSourcesDir: File
) {
    val model: IntermediateModel get() = taskParams.model
}

data class IntermediateFile(
    val filePath: String,
    val serviceKind: String,
    val date: String
)

class KohesiveAwsCodeGen(
    val outputDirectory: String,
    val mainSourceDirectory: String
) {

    companion object {
        // TODO: uncomment
        //  val IntermediateFilenameRegexp = "(.*)-(\\d{4}-\\d{2}-\\d{2})-intermediate\\.json".toRegex()
        val IntermediateFilenameRegexp = ".*(ec2)-(\\d{4}-\\d{2}-\\d{2})-intermediate\\.json".toRegex()
    }

    private val generatorTaskExecutor = GeneratorTaskExecutor()

    fun generate() {
        val baseContextData   = BaseContextData()
        val intermediateFiles = Reflections("models", ResourcesScanner()).getResources(IntermediateFilenameRegexp.toPattern())

        // Prepare intermediate data
        val recentIntermediateFiles = intermediateFiles.filter { it.matches(IntermediateFilenameRegexp) }.map { filePath ->
            val filename = filePath.substring(filePath.lastIndexOf('/')).drop(1)
            IntermediateFile(
                filePath    = filePath,
                serviceKind = IntermediateFilenameRegexp.find(filename)?.groupValues?.get(1)!!,
                date        = IntermediateFilenameRegexp.find(filename)?.groupValues?.get(2)!!
            )
        }.groupBy { it.serviceKind }.flatMap { it.value }.asSequence()

        // Generate straight from intermediate data
        recentIntermediateFiles.map { it.filePath }.forEach { modelFile ->
            ModelLoaderUtils.getRequiredResourceAsStream(modelFile).use { stream ->
                val intermediateModel: IntermediateModel = loadModel(stream)

                val params = KohesiveGenerateParams(
                    taskParams     = GeneratorTaskParams.create(intermediateModel, outputDirectory, outputDirectory),
                    mainSourcesDir = File(mainSourceDirectory)
                )

                val kohesiveGeneratorTasks = KohesiveGeneratorTasks(params, baseContextData)
                val emitter = CodeEmitter(kohesiveGeneratorTasks, generatorTaskExecutor)
                emitter.emit()
            }
        }

        // Generate from base context data
        val emitter = CodeEmitter(listOf(IacContextGeneratorTask.create(outputDirectory, baseContextData)), generatorTaskExecutor)
        emitter.emit()
    }

}

fun IntermediateModel.getShortServiceName() = metadata.syncInterface.replace("Amazon", "").replace("AWS", "")

class KohesiveGeneratorTasks(
    val params: KohesiveGenerateParams,
    val baseContextData: BaseContextData
) : BaseGeneratorTasks(params.taskParams) {

    override fun createTasks() = listOf(
        ServiceContextGeneratorTask.create(params, baseContextData),
        DeferredClientGeneratorTask.create(params, baseContextData)
    )

}

inline fun <reified T> loadModel(inputStream: InputStream): T {
    return Jackson.load(T::class.java, inputStream)
}