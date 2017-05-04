package uy.kohesive.iac.model.aws.cloudformation.codegen

import org.reflections.Reflections
import org.reflections.scanners.TypeElementsScanner
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.resources.CFResources
import uy.kohesive.iac.model.aws.codegen.AWSModelProvider
import uy.kohesive.iac.model.aws.utils.pluralize
import uy.kohesive.iac.model.aws.utils.simpleName
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

fun main(args: Array<String>) {
    ResourceBuildersCodeGen(
        outputDir   = "/Users/eliseyev/TMP/cf/model/",
        packageName = "uy.kohesive.iac.model.aws.cloudformation.resources.builders"
    ).generate()
}


class ResourceBuildersCodeGen(
    val outputDir: String,
    val packageName: String
) {

    var totalCFTypes = 0
    var foundRequestClasses = 0
    var matchingProperties = 0
    var mostlyMatchingProperties = 0

    val awsModelProvider = AWSModelProvider()

    fun generate() {
        CFResources.serviceResourceContainers.forEach { typesContainer ->
            typesContainer.simpleName?.toLowerCase()?.let { serviceName ->
                // TODO: extract to utils, create service aliases map
                val (serviceKey, serviceVersion) = if (serviceName == "applicationautoscaling") {
                    "autoscaling" to "2016-02-06"
                } else if (serviceName == "elasticloadbalancingv2") {
                    "elasticloadbalancing" to "2015-12-01"
                } else if (serviceName == "certificatemanager") {
                    "acm" to null
                } else if (serviceName == "cloudwatch") {
                    "monitoring" to null
                } else if (serviceName == "directoryservice") {
                    "ds" to null
                } else if (serviceName == "efs") {
                    "elasticfilesystem" to null
                } else if (serviceName == "elasticsearch") {
                    "es" to null
                } else if (serviceName == "kinesisfirehose") {
                    "kinesis" to null
                } else if (serviceName == "stepfunctions") {
                    "states" to null
                } else if (serviceName == "emr") {
                    "elasticmapreduce" to null
                } else {
                    serviceName to null
                }

                val awsModel     = awsModelProvider.getModel(serviceKey, serviceVersion)
                val modelPackage = (awsModel.metadata.packageName + ".model.").let {
                    if (serviceName == "kinesisfirehose") {
                        it.replace("kinesis", "kinesisfirehose")
                    } else {
                        it
                    }
                }

                val modelLcSimpleNameToFq = TypeElementsScanner().apply {
                    Reflections(modelPackage, this)
                }.store.keySet().associateBy(String::simpleName).mapKeys {
                    (if (it.key.contains("$")) {
                        it.key.substring(it.key.lastIndexOf('$') + 1)
                    } else {
                        it.key
                    }).toLowerCase()
                }

                typesContainer.nestedClasses.filter { nestedClass ->
                    nestedClass.findAnnotation<CloudFormationType>() != null
                }.forEach { nestedClass ->
                    totalCFTypes++

                    val cfType = nestedClass.findAnnotation<CloudFormationType>()?.value

                    // Let's find a corresponding request class
                    val supposedRequestNames = listOf("Create", "Put").flatMap {
                        listOf(
                            it + nestedClass.simpleName + "Request",
                            it + nestedClass.simpleName?.pluralize() + "Request"
                        )
                    }

                    val requestClass = supposedRequestNames.map { supposedRequestName ->
                        try {
                            modelLcSimpleNameToFq[supposedRequestName.toLowerCase()]?.let { modelClassFqName ->
                                Class.forName(modelClassFqName)
                            }
                        } catch (t: Throwable) {
                            null
                        }
                    }.filterNotNull().firstOrNull()

                    if (requestClass == null) {
                        println("No model class for CF resource $cfType")
                    } else {
                        foundRequestClasses++

                        // Let's check if we have all the needed properties in the model class
                        val cfClassProperties    = nestedClass.memberProperties.map { it.name }
                        val modelClassProperties = requestClass.methods.filter { it.name.startsWith("get") }.map {
                            it.name.replaceFirst("get", "")
                        }

                        if (modelClassProperties.containsAll(cfClassProperties)) {
                            matchingProperties++
                            mostlyMatchingProperties++
                        } else {
                            val missingProperties = cfClassProperties.filterNot { modelClassProperties.contains(it) }
                            if (missingProperties.size < cfClassProperties.size / 2) {
                                mostlyMatchingProperties++
                            }
                        }
                    }
                }
            }
        }

        println("foundRequestClasses: $foundRequestClasses")
        println("matchingProperties: $matchingProperties")
        println("mostlyMatchingProperties: $mostlyMatchingProperties")
        println("totalCFTypes: $totalCFTypes")
    }

}