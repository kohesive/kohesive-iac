package uy.kohesive.iac.model.aws.cloudformation.crawler

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

fun main(args: Array<String>) {
    val resources = DocumentationCrawler(
        baseUri       = "/Users/eliseyev/TMP/cf/",
        localMode     = true,
        downloadFiles = true
    ).crawl()

    SchemaWriter(File("/Users/eliseyev/git/kohesive-iac/model-aws/src/generated/resources/cfSchema/")).write(resources)
}

class SchemaWriter(
    val outputDir: File
) {

    companion object {
        val JsonPrettyWriter = jacksonObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .writerWithDefaultPrettyPrinter()
    }

    fun write(resources: List<CloudFormationResource>) {
        resources.groupBy { (_, resourceType) ->
            resourceType.split("::")[1]
        }.forEach { group, resources ->
            JsonPrettyWriter.writeValue(File(outputDir, "$group.json"), resources)
        }

        println("Done writing to ${outputDir.path}")
    }

}