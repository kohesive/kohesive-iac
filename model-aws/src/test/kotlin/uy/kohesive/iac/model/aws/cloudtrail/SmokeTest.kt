package uy.kohesive.iac.model.aws.cloudtrail

import uy.kohesive.iac.model.aws.codegen.TemplateDescriptor
import java.io.File

fun main(args: Array<String>) {
    val packageName = "uy.kohesive.iac.model.aws.cloudtrail.test"
    val outputDir   = File("/Users/eliseyev/TMP/codegen/runners/", packageName.replace('.', '/')).apply { mkdirs() }

    val awsModelProvider = AWSModelProvider()
    var counter = 0

    EventsProcessor(
        eventsDir       = File("/Users/eliseyev/Downloads/CloudTrail2/us-east-1/2017/02/16/"),
//        eventsDir       = File("/Users/eliseyev/Downloads/CloudTrail2/"),
        oneEventPerFile = false,
        gzipped         = true
    ).process { event ->
        if (listOf("Create", "Put", "Attach", "Run", "Set").any { event.eventName.startsWith(it) }) {
            val serviceName = event.eventSource.split('.').first()

            val awsModel = try {
                awsModelProvider.getModel(serviceName, event.apiVersion)
            } catch (t: Throwable) {
                throw RuntimeException("Can't obtain an AWS model for $event", t)
            }

            val className    = "${event.eventName}Runner_${counter++}"
            val classContent = AWSApiCallBuilder(
                intermediateModel = awsModel,
                event             = event,
                packageName       = packageName,
                className         = className
            ).build(TemplateDescriptor.RequestRunner)

            File(outputDir, "$className.kt").writeText(classContent)
        }
    }.forEach {  }
}
