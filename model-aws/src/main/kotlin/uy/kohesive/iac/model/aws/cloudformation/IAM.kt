package uy.kohesive.iac.model.aws.cloudformation

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.identitymanagement.model.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class IamInstanceProfilePropertiesBuilder : ResourcePropertiesBuilder<CreateInstanceProfileRequest> {
    override val requestClazz = CreateInstanceProfileRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateInstanceProfileRequest).let {
            IamInstanceProfileProperties(
                Path  = request.path ?: "/",
                Roles = relatedObjects.filterIsInstance(AddRoleToInstanceProfileRequest::class.java).map {
                    it.roleName
                }
            )
        }
}

class IamPolicyResourcePropertiesBuilder : ResourcePropertiesBuilder<CreatePolicyRequest> {
    override val requestClazz = CreatePolicyRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreatePolicyRequest).let {
            IamPolicyResourceProperties(
                PolicyName     = request.policyName,
                PolicyDocument = jacksonObjectMapper().readTree(request.policyDocument),
                Roles          = relatedObjects.filterIsInstance(AttachRolePolicyRequest::class.java).map {
                    it.roleName
                }
            )
        }
}

class IamRoleResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateRoleRequest> {
    override val requestClazz = CreateRoleRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateRoleRequest).let {
            IamRoleResourceProperties(
                AssumeRolePolicyDocument = jacksonObjectMapper().readTree(request.assumeRolePolicyDocument),
                Path = request.path
            )
        }
}

data class IamRoleResourceProperties(
    val AssumeRolePolicyDocument: Any?,
    val Path: String?
) : ResourceProperties

data class IamPolicyResourceProperties(
    val PolicyName: String,
    val PolicyDocument: Any,
    val Roles: List<Any>?
) : ResourceProperties

data class IamInstanceProfileProperties(
    val Path: String,
    val Roles: List<Any>?
) : ResourceProperties