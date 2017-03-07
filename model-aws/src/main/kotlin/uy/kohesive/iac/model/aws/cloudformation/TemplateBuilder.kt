package uy.kohesive.iac.model.aws.cloudformation

import com.amazonaws.AmazonWebServiceRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.AwsTypes
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.ParameterizedValue
import uy.kohesive.iac.model.aws.cloudformation.processing.TemplateProcessor
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class TemplateBuilder(
    val context: IacContext,
    val description: String? = null,
    val version: String = "2010-09-09"
) {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    fun build(): JsonNode {
        val rawTemplate = Template(
            Description              = description,
            AWSTemplateFormatVersion = version,
            Parameters               = context.variables.mapValues { varEntry ->
                varEntry.value.toCFParameter()
            },
            Resources = context.objectsToNames.toList().groupBy(Pair<Any, String>::second).mapValues {
                it.value.map { it.first }
            }.mapValues {
                val objectsWithSameName = it.value

                objectsWithSameName.firstOrNull { obj ->
                    (obj as? AmazonWebServiceRequest)?.let { request ->
                        AwsTypes.isCreationRequestClass(request::class)
                    } ?: false
                }?.let { creationRequest ->
                    val awsType = AwsTypes.fromClass(creationRequest::class)

                    Resource(
                        Type       = awsType.type,
                        Properties = ResourcePropertyBuilders.getBuilder(awsType)?.buildResource(
                            creationRequest as AmazonWebServiceRequest,
                            objectsWithSameName
                        )
                    )
                }
            }.filterValues { it != null }.mapValues { it.value!! }
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
    var Resources: Map<String, Resource> = emptyMap()
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

data class Resource(
    var Type: String,
    var Properties: ResourceProperties? = null,
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