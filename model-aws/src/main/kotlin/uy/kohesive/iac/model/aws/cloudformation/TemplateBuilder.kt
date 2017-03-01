package uy.kohesive.iac.model.aws.cloudformation

import com.amazonaws.AmazonWebServiceRequest
import uy.kohesive.iac.model.aws.AwsTypes
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.ParameterizedValue

class TemplateBuilder(
    val context: IacContext,
    val description: String? = null,
    val version: String = "2010-09-09"
) {

    fun build(): Template {
        return Template(
            Description = description,
            AWSTemplateFormatVersion = version,
            Parameters = context.variables.mapValues { varEntry ->
                varEntry.value.toCFParameter()
            },
            Resources = context.objectsToNames.toList().groupBy(Pair<Any, String>::second).mapValues {
                it.value.map { it.first }
            }.mapValues {
                val objectsWithSameName = it.value

                objectsWithSameName.firstOrNull{ obj ->
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
    }
}

data class Template(
    val AWSTemplateFormatVersion: String?,
    val Description: String?,
    val Parameters: Map<String, Parameter> = emptyMap(),
    val Mappings: Map<String, Any> = emptyMap(),
    val Resources: Map<String, Resource> = emptyMap()
)

data class Parameter(
    val Description: String?,
    val Type: String,
    val Default: String?,
    val MinLength: String?,
    val MaxLength: String?,
    val AllowedValues: List<String>?,
    val AllowedPattern: String?,
    val ConstraintDescription: String?
)

interface ResourceProperties

data class Resource(
    val Type: String,
    val Properties: ResourceProperties? = null,
    val Metadata: Map<String, Any>? = emptyMap()
)

fun ParameterizedValue.toCFParameter(): Parameter = Parameter(
    Description           = description,
    Type                  = type.cloudFormationName,
    AllowedPattern        = allowedPattern?.pattern,
    AllowedValues         = allowedValues,
    ConstraintDescription = constraintDescription,
    Default               = defaultValue,
    MaxLength             = allowedLength?.endInclusive?.toString(),
    MinLength             = allowedLength?.start?.toString()
)