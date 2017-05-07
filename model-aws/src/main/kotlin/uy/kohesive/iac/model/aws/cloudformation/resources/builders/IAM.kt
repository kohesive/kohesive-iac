package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.identitymanagement.model.*
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.IAM
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class IAMManagedPolicyResourcePropertiesBuilder : ResourcePropertiesBuilder<CreatePolicyRequest> {

    override val requestClazz = CreatePolicyRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreatePolicyRequest).let {
            IAM.ManagedPolicy(
                Description    = it.description,
                PolicyDocument = it.policyDocument.let { IAMGroupResourcePropertiesBuilder.JSON.readValue<Map<String, Any>>(it) },
                Path           = it.path
            )
        }
}

class IAMAccessKeyResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateAccessKeyRequest> {

    override val requestClazz = CreateAccessKeyRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAccessKeyRequest).let {
            IAM.AccessKey(
                UserName = request.userName,
                Status   = relatedObjects.filterIsInstance<UpdateAccessKeyRequest>().lastOrNull()?.status ?: "Active"
            )
        }

}

class IAMGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateGroupRequest> {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }


    override val requestClazz = CreateGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateGroupRequest).let {
            IAM.Group(
                GroupName         = request.groupName,
                Path              = request.path,
                ManagedPolicyArns = relatedObjects.filterIsInstance<AttachGroupPolicyRequest>().map { it.policyArn },
                Policies          = relatedObjects.filterIsInstance<PutGroupPolicyRequest>().map {
                    IAM.Role.PolicyProperty(
                        PolicyName     = it.policyName,
                        PolicyDocument = it.policyDocument.let { JSON.readValue<Map<String, Any>>(it) }
                    )
                }
            )
        }

}

class IAMUserResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateUserRequest> {

    override val requestClazz = CreateUserRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateUserRequest).let {
            IAM.User(
                Path     = request.path,
                UserName = request.userName,
                Groups   = relatedObjects.filterIsInstance<AddUserToGroupRequest>().map {
                    it.groupName
                },
                LoginProfile = relatedObjects.filterIsInstance<CreateLoginProfileRequest>().lastOrNull()?.let {
                    IAM.User.LoginProfileProperty(
                        Password              = it.password,
                        PasswordResetRequired = it.passwordResetRequired?.toString()
                    )
                },
                ManagedPolicyArns = relatedObjects.filterIsInstance<AttachUserPolicyRequest>().map { it.policyArn },
                Policies          = relatedObjects.filterIsInstance<PutUserPolicyRequest>().map {
                    IAM.Role.PolicyProperty(
                        PolicyName     = it.policyName,
                        PolicyDocument = it.policyDocument.let { IAMGroupResourcePropertiesBuilder.JSON.readValue<Map<String, Any>>(it) }
                    )
                }
            )
        }

}

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
                Path                     = request.path,
                ManagedPolicyArns        = relatedObjects.filterIsInstance<AttachRolePolicyRequest>().map { it.policyArn },
                Policies                 = relatedObjects.filterIsInstance<PutRolePolicyRequest>().map {
                    IAM.Role.PolicyProperty(
                        PolicyName     = it.policyName,
                        PolicyDocument = it.policyDocument.let { IAMGroupResourcePropertiesBuilder.JSON.readValue<Map<String, Any>>(it) }
                    )
                },
                RoleName = it.roleName
            )
        }
}