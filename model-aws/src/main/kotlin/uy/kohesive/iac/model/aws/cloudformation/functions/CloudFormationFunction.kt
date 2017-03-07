package uy.kohesive.iac.model.aws.cloudformation.functions

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize

interface CloudFormationFunction {
    val name: String
    val args: Any?
}

@JsonSerialize(using = CloudFormationFunctionSerializer::class)
class CFBase64Function(value: Any?) : CloudFormationFunction {
    override val name = "Fn::Base64"
    override val args = value
}

@JsonSerialize(using = CloudFormationFunctionSerializer::class)
class CFJoinFunction(args: List<Any>, delimiter: Any = "") : CloudFormationFunction {
    override val name = "Fn::Join"
    override val args = listOf(delimiter) + listOf(args)
}

@JsonSerialize(using = CloudFormationFunctionSerializer::class)
class CFFindInMapFunction(mapName: Any, topLevelKey: Any, secondLevelKey: Any) : CloudFormationFunction {
    override val name = "Fn::FindInMap"
    override val args = listOf(mapName, topLevelKey, secondLevelKey)
}

@JsonSerialize(using = CloudFormationFunctionSerializer::class)
class CFRefFunction(target: Any) : CloudFormationFunction {
    override val name = "Ref"
    override val args = target
}

@JsonSerialize(using = CloudFormationFunctionSerializer::class)
class CFGetAvailabilityZonesFunction(arg: String = "") : CloudFormationFunction {
    override val name = "Fn::GetAZs"
    override val args = arg
}

@JsonSerialize(using = CloudFormationFunctionSerializer::class)
class CFGetAttributeFunction(logicalNameOfResource: Any, attributeName: Any) : CloudFormationFunction {
    override val name = "Fn::GetAtt"
    override val args = listOf(logicalNameOfResource, attributeName)
}

class CloudFormationFunctionSerializer : JsonSerializer<CloudFormationFunction>() {

    override fun serialize(value: CloudFormationFunction, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartObject()
        gen.writeObjectField(value.name, value.args)
        gen.writeEndObject()
    }

}
