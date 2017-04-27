package uy.kohesive.iac.model.aws.cloudtrail.command

import io.airlift.airline.Command
import io.airlift.airline.Option
import uy.kohesive.iac.model.aws.cloudtrail.AWSApiCallBuilder
import uy.kohesive.iac.model.aws.cloudtrail.AWSModelProvider
import uy.kohesive.iac.model.aws.codegen.TemplateDescriptor
import java.io.File

@Command(name = "codegen", description = "Generates AWS API calls code from CloudTrail events fetched via API or stored on a local filesystem")
class CodegenEventsCommand : BaseCloudTrailCommand(), Runnable {

    @Option(name = arrayOf("--outputDir"), description = "Output base directory path", required = true)
    lateinit var outputDir: String

    @Option(name = arrayOf("--packageName"), description = "Generated classes package name", required = true)
    lateinit var packageName: String

    val awsModelProvider = AWSModelProvider()

    override fun run() {
        var counter = 0

        createEventsProcessor().process { event ->
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

                File(outputDir, "$className.kt").apply {
                    writeText(classContent)
                }
            } else {
                null
            }
        }.filterNotNull().forEach { outputFile ->
            println("Done writing ${outputFile.path}")
        }
    }

}