package uy.kohesive.iac.model.aws

import kotlin.reflect.KClass

interface IacContextNamingStrategy {
    // Type is like "AWS::IAM::Role"
    fun nameFor(environment: String, type: String, localName: String): String

    // Type is like CreateRoleRequest::class
    fun <T : Any> nameFor(environment: String, relatedClass: KClass<out T>, localName: String): String = nameFor(environment, AwsTypes.fromClass(relatedClass).type, localName)
}