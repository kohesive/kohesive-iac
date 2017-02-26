package uy.kohesive.iac.model.aws

import com.amazonaws.services.identitymanagement.model.CreateRoleRequest
import kotlin.reflect.KClass

interface IacContextNamingStrategy {
    // Type is like "AWS::IAM::Role"
    fun nameFor(environment: String, type: String, localName: String): String

    // Type is like CreateRoleRequest::class
    fun <T: Any> nameFor(environment: String, type: KClass<T>, localName: String): String = nameFor(environment, awsTypeFromRequestClass(type), localName)
}