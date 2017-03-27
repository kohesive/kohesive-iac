package uy.kohesive.iac.model.aws.cloudformation.codegen

import com.amazonaws.codegen.emitters.CodeEmitter
import com.amazonaws.codegen.emitters.CodeWriter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.emitters.GeneratorTaskExecutor
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import freemarker.template.Template
import uy.kohesive.iac.model.aws.cloudformation.crawler.CloudFormationResource
import uy.kohesive.iac.model.aws.codegen.TemplateDescriptor
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy
import java.io.File
import java.io.Writer

fun main(args: Array<String>) {
    CloudFormationModelCodeGen(
        inputDir    = "./model-aws/src/generated/resources/cfSchema",
        outputDir   = "/Users/eliseyev/TMP/cf/model/",
        packageName = "uy.kohesive.iac.model.aws.cloudformation.resources"
    ).generate()
}

// TODO: annotation to hold AWS CF type name
class CloudFormationModelCodeGen(
    val inputDir: String,
    val outputDir: String,
    val packageName: String
) {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    private val generatorTaskExecutor = GeneratorTaskExecutor()

    fun generate() {
        File(inputDir).listFiles { _, name ->
            name.endsWith(".json", ignoreCase = true)
        }.forEach { file ->
            val model = AmazonServiceCFModel(
                serviceName = file.nameWithoutExtension,
                resources   = JSON.readValue<List<CloudFormationResource>>(file)
            )
            val generateTask = AmazonServiceCFModelGenerateTask.create(
                outputDir   = outputDir,
                packageName = packageName,
                cfModel     = model
            )

            val emitter = CodeEmitter(listOf(generateTask), generatorTaskExecutor)
            emitter.emit()
        }
    }

}

class AmazonServiceCFModelGenerateTask private constructor(writer: Writer, template: Template, data: Any)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(outputDir: String, packageName: String, cfModel: AmazonServiceCFModel): AmazonServiceCFModelGenerateTask {
            return AmazonServiceCFModelGenerateTask(
                CodeWriter(
                    outputDir + "/" + packageName.replace('.', '/'),
                    cfModel.serviceName,
                    ".kt"
                ),
                TemplateDescriptor.CloudFormationModel.load(),
                AmazonCFServiceModel(
                    packageName = packageName,
                    serviceName = cfModel.serviceName,
                    classes     = ModelBuilder(cfModel.resources).build()
                )
            )
        }
    }

}

data class AmazonServiceCFModel(
    val serviceName: String,
    val resources: List<CloudFormationResource>
)
