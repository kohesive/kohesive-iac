package uy.kohesive.iac.model.aws.cloudformation

import com.amazonaws.AmazonWebServiceRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.AwsTypes
import uy.kohesive.iac.model.aws.ParameterizedValue
import uy.kohesive.iac.model.aws.cloudformation.processing.TemplateProcessor
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class TemplateBuilder(
    val context: CloudFormationContext,
    val description: String? = null,
    val version: String = "2010-09-09"
) {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    fun build(): JsonNode {
        val nameToDependsOnNames = context.dependsOn.map { entry ->
            val sourceName  = context.getNameStrict(entry.key)
            val targetNames = entry.value.map { targetObj ->
                context.getNameStrict(targetObj)
            }.distinct()

            sourceName to targetNames
        }.toMap()

        val rawTemplate = Template(
            Description              = description,
            AWSTemplateFormatVersion = version,
            Parameters = context.variables.mapValues { varEntry ->
                varEntry.value.toCFParameter()
            },
            Mappings = context.mappings.map {
                it.key to it.value.map
            }.toMap(),
            Resources = context.objectsToNames.toList().groupBy(Pair<Any, String>::second).mapValues {
                it.value.map { it.first }
            }.mapValues {
                val name = it.key
                val objectsWithSameName = it.value

                objectsWithSameName.firstOrNull { obj ->
                    (obj as? AmazonWebServiceRequest)?.let { request ->
                        AwsTypes.isCreationRequestClass(request::class)
                    } ?: false
                }?.let { creationRequest ->
                    val awsType = AwsTypes.fromClass(creationRequest::class)
                    val amazonWebServiceRequest = creationRequest as AmazonWebServiceRequest

                    ResourcePropertyBuilders.getBuilder(awsType)?.takeIf {
                        it.canBuildFrom(amazonWebServiceRequest)
                    }?.let { resourceBuilder ->
                        Resource(
                            Type       = awsType.type,
                            Properties = resourceBuilder.buildResource(
                                amazonWebServiceRequest,
                                objectsWithSameName
                            ),
                            DependsOn = nameToDependsOnNames[name]?.let { dependOnNames ->
                                if (dependOnNames.size == 1) {
                                    dependOnNames[0]
                                } else {
                                    dependOnNames
                                }
                            }
                        )
                    }
                }
            }.filterValues { it != null }.mapValues { it.value!! },
            Outputs = context.outputs.map {
                it.logicalId to Output(
                    Value       = it.value,
                    Description = it.description
                )
            }.toMap()
        )

        val templateTree = JSON.valueToTree<ObjectNode>(rawTemplate)
        TemplateProcessor(context).process(templateTree)
        return templateTree
    }
}

data class Template(
    var AWSTemplateFormatVersion: String?,
    var Description: String?,
    var Parameters: Map<String, Parameter> = emptyMap(),
    var Mappings: Map<String, Any> = emptyMap(),
    var Resources: Map<String, Resource> = emptyMap(),
    val Outputs: Map<String, Output> = emptyMap()
)

data class Output(
    val Description: String?,
    val Value: String
)

data class Parameter(
    var Description: String?,
    var Type: String,
    var Default: String?,
    var MinLength: String?,
    var MaxLength: String?,
    var AllowedValues: List<String>?,
    var AllowedPattern: String?,
    var ConstraintDescription: String?
)

interface ResourceProperties

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CloudFormationType(val value: String)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CloudFormationTypes

data class Resource(
    var Type: String,
    var Properties: ResourceProperties? = null,
    val DependsOn: Any?, // String or List<String>
    var Metadata: Map<String, Any>? = emptyMap()
)

fun ParameterizedValue<out Any>.toCFParameter(): Parameter = Parameter(
    Description           = description,
    Type                  = type.cloudFormationName,
    AllowedPattern        = allowedPattern?.pattern,
    AllowedValues         = allowedValues,
    ConstraintDescription = constraintDescription,
    Default               = defaultValue,
    MaxLength             = allowedLength?.endInclusive?.toString(),
    MinLength             = allowedLength?.start?.toString()
)