package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.identitymanagement.model.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.IAM

class IamInstanceProfilePropertiesBuilder : ResourcePropertiesBuilder<CreateInstanceProfileRequest> {
    override val requestClazz = CreateInstanceProfileRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateInstanceProfileRequest).let {
            IAM.InstanceProfile(
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
            IAM.Policy(
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
            IAM.Role(
                AssumeRolePolicyDocument = jacksonObjectMapper().readTree(request.assumeRolePolicyDocument),
                Path = request.path
            )
        }
}