package uy.kohesive.iac.model.aws.cloudtrail

import uy.kohesive.iac.model.aws.codegen.TemplateDescriptor
import java.io.File

fun main(args: Array<String>) {
    val awsModelProvider = AWSModelProvider()

    EventsProcessor(
        eventsDir       = File("/Users/eliseyev/Downloads/CloudTrail2/"),
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

            AWSApiCallBuilder(awsModel, event).build(TemplateDescriptor.RequestBuilder)
        } else {
            null
        }
    }.filterNotNull().forEach(::println)
}
