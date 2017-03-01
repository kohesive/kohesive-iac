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
            // TODO: This is wrong, we need to group all the objects (request, response & related) by id/name
            // TODO: so we can, for example, see the attach policy to role request in role creation.
            // TODO: To do this, we need to figure out the auto-naming VS ids first.
            Resources = context.objectsToIds.filterKeys { obj ->
                (obj as? AmazonWebServiceRequest)?.let { request ->
                    AwsTypes.isCreationRequestClass(request::class)
                } ?: false
            }.map { entry ->
                val name = entry.value
                val obj  = entry.key

                val awsType = AwsTypes.fromClass(obj::class)

                name to Resource(
                    Type = awsType.type
                )
            }.toMap()
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

data class Resource(
    val Type: String,
    val Properties: Map<String, Any>? = emptyMap(),
    val Metadata: Map<String, Any>? = emptyMap()
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