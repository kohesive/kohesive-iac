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
    val stream = ModelLoaderUtils.getRequiredResourceAsStream("models/autoscaling-2016-02-06-intermediate.json")
    val intermediateModel: IntermediateModel = loadModel(stream)

    val outputDirectory = File("/Users/eliseyev/TMP/codegen/").path
    val params = GeneratorTaskParams.create(intermediateModel, outputDirectory, outputDirectory)

    val kohesiveGeneratorTasks = KohesiveGeneratorTasks(params)
    val emitter = CodeEmitter(kohesiveGeneratorTasks, GeneratorTaskExecutor())
    emitter.emit()
}

class KohesiveGeneratorTasks(params: GeneratorTaskParams) : BaseGeneratorTasks(params) {

    private val baseDirectory = params.pathProvider.basePackageDirectory

    override fun createTasks() = listOf(
        createSomeTask()
    )

    private fun createSomeTask() = FreemarkerGeneratorTask(
        baseDirectory,
        model.metadata.syncClient,
        freemarker.syncClientTemplate,
        model
    )

}

inline fun <reified T> loadModel(inputStream: InputStream): T {
    return Jackson.load(T::class.java, inputStream)
}