package uy.kohesive.iac.model.aws

import com.amazonaws.services.identitymanagement.model.CreateInstanceProfileRequest
import com.amazonaws.services.identitymanagement.model.CreatePolicyRequest
import com.amazonaws.services.identitymanagement.model.CreateRoleRequest
import kotlin.reflect.KClass

private val awsRequestClassToTypeMappings = mapOf<KClass<out Any>, String>(
        CreateRoleRequest::class to "AWS::IAM::Role",
        CreatePolicyRequest::class to "AWS::IAM::Policy",
        CreateInstanceProfileRequest::class to "AWS::IAM::InstanceProfile"
)

private val typeToAwsRequestClassMappings = awsRequestClassToTypeMappings.map { it.value to it.key }.toMap()

fun <T : Any> awsTypeFromRequestClass(type: KClass<T>): String {
    return awsRequestClassToTypeMappings.get(type) ?: throw IllegalArgumentException("type ${type.simpleName} is not a known AWS type")
}

fun <T: Any> awsRequestClassFromType(type: String): KClass<T> {
    @Suppress("UNCHECKED_CAST")
    return typeToAwsRequestClassMappings.get(type) as? KClass<T> ?: throw IllegalArgumentException("type ${type} is not a known AWS type")
}