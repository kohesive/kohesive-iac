package uy.kohesive.iac.model.aws.cloudformation

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
            }
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
    val Properties: Map<String, Any>?,
    val Metadata: Map<String, Any>?
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