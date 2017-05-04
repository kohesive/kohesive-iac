package uy.kohesive.iac.model.aws.cloudformation.codegen

import com.amazonaws.codegen.emitters.CodeEmitter
import com.amazonaws.codegen.emitters.CodeWriter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.emitters.GeneratorTaskExecutor
import freemarker.template.Template
import org.reflections.Reflections
import org.reflections.scanners.TypeElementsScanner
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.resources.CFResources
import uy.kohesive.iac.model.aws.codegen.AWSModelProvider
import uy.kohesive.iac.model.aws.codegen.TemplateDescriptor
import uy.kohesive.iac.model.aws.utils.firstLetterToLowerCase
import uy.kohesive.iac.model.aws.utils.pluralize
import uy.kohesive.iac.model.aws.utils.simpleName
import java.io.Writer
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

    private val generatorTaskExecutor = GeneratorTaskExecutor()

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

                val missingResources = ArrayList<String>()

                val awsModel     = awsModelProvider.getModel(serviceKey, serviceVersion)
                val modelPackage = (awsModel.metadata.packageName + ".model").let {
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
                }.map { nestedClass ->
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
                        missingResources.add(cfType!!)
                        null
                    } else {
                        foundRequestClasses++

                        // Let's check if we have all the needed properties in the model class
                        val cfClassProperties    = nestedClass.memberProperties.map { it.name }
                        val modelClassProperties = requestClass.methods.filter { it.name.startsWith("get") }.map {
                            it.name.replaceFirst("get", "")
                        }

                        var missingProperties: List<String> = emptyList()

                        if (modelClassProperties.containsAll(cfClassProperties)) {
                            matchingProperties++
                            mostlyMatchingProperties++
                        } else {
                            missingProperties = cfClassProperties.filterNot { modelClassProperties.contains(it) }
                            if (missingProperties.size <= cfClassProperties.size / 2) {
                                mostlyMatchingProperties++
                            }
                        }

                        val matchingProperties = cfClassProperties.intersect(modelClassProperties)

                        Resource(
                            entityName        = nestedClass.simpleName!!,
                            requestName       = requestClass.simpleName,
                            missingProperties = missingProperties,
                            properties        = matchingProperties.map {
                                Property(
                                    name   = it,
                                    nameLC = it.firstLetterToLowerCase()
                                )
                            }
                        )
                    }
                }.filterNotNull().takeIf { it.isNotEmpty() }?.let { resources ->
                    val codegenModel     = CFResourcesBuilderTemplateModel(
                        modelPackage     = modelPackage,
                        packageName      = packageName,
                        serviceName      = typesContainer.simpleName!!,
                        resources        = resources,
                        missingResources = missingResources
                    )

                    val generateTask = CFResourcesBuilderTemplateGenerateTask.create(
                        model       = codegenModel,
                        packageName = packageName,
                        outputDir   = outputDir
                    )

                    val emitter = CodeEmitter(listOf(generateTask), generatorTaskExecutor)
                    emitter.emit()
                }
            }
        }

        println("foundRequestClasses: $foundRequestClasses")
        println("matchingProperties: $matchingProperties")
        println("mostlyMatchingProperties: $mostlyMatchingProperties")
        println("totalCFTypes: $totalCFTypes")

        generatorTaskExecutor.waitForCompletion()
        generatorTaskExecutor.shutdown()
    }

}

data class CFResourcesBuilderTemplateModel(
    val packageName: String,
    val modelPackage: String,
    val serviceName: String,
    val missingResources: List<String>,
    val resources: List<Resource>
)

data class Resource(
    val entityName: String,
    val requestName: String,
    val missingProperties: List<String>,
    val properties: List<Property>
)

data class Property(
    val name: String,
    val nameLC: String
)

class CFResourcesBuilderTemplateGenerateTask private constructor(writer: Writer, template: Template, data: Any)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(outputDir: String, packageName: String, model: CFResourcesBuilderTemplateModel): CFResourcesBuilderTemplateGenerateTask {
            return CFResourcesBuilderTemplateGenerateTask(
                CodeWriter(
                    outputDir + "/" + packageName.replace('.', '/'),
                    model.serviceName,
                    ".kt"
                ),
                TemplateDescriptor.CloudFormationResourceBuilder.load(),
                model
            )
        }
    }

}