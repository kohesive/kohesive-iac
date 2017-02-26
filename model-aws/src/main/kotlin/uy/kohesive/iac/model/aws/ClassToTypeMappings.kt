package uy.kohesive.iac.model.aws

import com.amazonaws.services.identitymanagement.model.CreatePolicyRequest
import com.amazonaws.services.identitymanagement.model.CreateRoleRequest
import kotlin.reflect.KClass

private val awsClassToTypeMappings = mapOf<KClass<out Any>, String>(
        CreateRoleRequest::class to "AWS::IAM::Role",
        CreatePolicyRequest::class to "AWS::IAM::Policy"
)

fun <T : Any> awsTypeFromClass(type: KClass<T>): String {
    return awsClassToTypeMappings.get(type) ?: throw IllegalArgumentException("type ${type.simpleName} is not a known AWS type")
}