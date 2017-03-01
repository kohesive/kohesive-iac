package uy.kohesive.iac.model.aws.cloudformation

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.identitymanagement.model.AddRoleToInstanceProfileRequest
import com.amazonaws.services.identitymanagement.model.AttachRolePolicyRequest
import com.amazonaws.services.identitymanagement.model.CreateInstanceProfileRequest
import com.amazonaws.services.identitymanagement.model.CreatePolicyRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class IamInstanceProfilePropertiesBuilder : ResourcePropertiesBuilder<CreateInstanceProfileRequest> {
    override val requestClazz = CreateInstanceProfileRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateInstanceProfileRequest).let {
            IamInstanceProfileProperties(
                Path  = it.path ?: "/",
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
                PolicyName     = it.policyName,
                PolicyDocument = jacksonObjectMapper().readTree(it.policyDocument),
                Roles          = relatedObjects.filterIsInstance(AttachRolePolicyRequest::class.java).map {
                    it.roleName
                }
            )
        }
}

data class IamPolicyResourceProperties(
    val PolicyName: String,
    val PolicyDocument: Any,
    val Roles: List<Any>?
) : ResourceProperties

data class IamInstanceProfileProperties(
    val Path: String,
    val Roles: List<Any>?
) : ResourceProperties