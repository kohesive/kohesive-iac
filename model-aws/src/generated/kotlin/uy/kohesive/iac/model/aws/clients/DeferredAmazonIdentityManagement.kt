package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.identitymanagement.AbstractAmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonIdentityManagement(val context: IacContext) : AbstractAmazonIdentityManagement(), AmazonIdentityManagement {

    override fun createAccessKey(request: CreateAccessKeyRequest): CreateAccessKeyResult {
        return with (context) {
            request.registerWithAutoName()
            CreateAccessKeyResult().withAccessKey(
                makeProxy<CreateAccessKeyRequest, AccessKey>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateAccessKeyRequest::getUserName to AccessKey::getUserName
                    )
                )
            )
        }
    }

    override fun createAccountAlias(request: CreateAccountAliasRequest): CreateAccountAliasResult {
        return with (context) {
            request.registerWithAutoName()
            CreateAccountAliasResult().registerWithSameNameAs(request)
        }
    }

    override fun createGroup(request: CreateGroupRequest): CreateGroupResult {
        return with (context) {
            request.registerWithAutoName()
            CreateGroupResult().withGroup(
                makeProxy<CreateGroupRequest, Group>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateGroupRequest::getPath to Group::getPath,
                        CreateGroupRequest::getGroupName to Group::getGroupName
                    )
                )
            )
        }
    }

    override fun createInstanceProfile(request: CreateInstanceProfileRequest): CreateInstanceProfileResult {
        return with (context) {
            request.registerWithAutoName()
            CreateInstanceProfileResult().withInstanceProfile(
                makeProxy<CreateInstanceProfileRequest, InstanceProfile>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateInstanceProfileRequest::getPath to InstanceProfile::getPath,
                        CreateInstanceProfileRequest::getInstanceProfileName to InstanceProfile::getInstanceProfileName
                    )
                )
            )
        }
    }

    override fun createLoginProfile(request: CreateLoginProfileRequest): CreateLoginProfileResult {
        return with (context) {
            request.registerWithAutoName()
            CreateLoginProfileResult().withLoginProfile(
                makeProxy<CreateLoginProfileRequest, LoginProfile>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateLoginProfileRequest::getUserName to LoginProfile::getUserName,
                        CreateLoginProfileRequest::getPasswordResetRequired to LoginProfile::getPasswordResetRequired
                    )
                )
            )
        }
    }

    override fun createOpenIDConnectProvider(request: CreateOpenIDConnectProviderRequest): CreateOpenIDConnectProviderResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateOpenIDConnectProviderRequest, CreateOpenIDConnectProviderResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createPolicy(request: CreatePolicyRequest): CreatePolicyResult {
        return with (context) {
            request.registerWithAutoName()
            CreatePolicyResult().withPolicy(
                makeProxy<CreatePolicyRequest, Policy>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreatePolicyRequest::getPolicyName to Policy::getPolicyName,
                        CreatePolicyRequest::getPath to Policy::getPath,
                        CreatePolicyRequest::getDescription to Policy::getDescription
                    )
                )
            )
        }
    }

    override fun createPolicyVersion(request: CreatePolicyVersionRequest): CreatePolicyVersionResult {
        return with (context) {
            request.registerWithAutoName()
            CreatePolicyVersionResult().withPolicyVersion(
                makeProxy<CreatePolicyVersionRequest, PolicyVersion>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request
                )
            )
        }
    }

    override fun createRole(request: CreateRoleRequest): CreateRoleResult {
        return with (context) {
            request.registerWithAutoName()
            CreateRoleResult().withRole(
                makeProxy<CreateRoleRequest, Role>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateRoleRequest::getPath to Role::getPath,
                        CreateRoleRequest::getRoleName to Role::getRoleName,
                        CreateRoleRequest::getAssumeRolePolicyDocument to Role::getAssumeRolePolicyDocument
                    )
                )
            )
        }
    }

    override fun createSAMLProvider(request: CreateSAMLProviderRequest): CreateSAMLProviderResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateSAMLProviderRequest, CreateSAMLProviderResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createServiceSpecificCredential(request: CreateServiceSpecificCredentialRequest): CreateServiceSpecificCredentialResult {
        return with (context) {
            request.registerWithAutoName()
            CreateServiceSpecificCredentialResult().withServiceSpecificCredential(
                makeProxy<CreateServiceSpecificCredentialRequest, ServiceSpecificCredential>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateServiceSpecificCredentialRequest::getServiceName to ServiceSpecificCredential::getServiceName,
                        CreateServiceSpecificCredentialRequest::getUserName to ServiceSpecificCredential::getUserName
                    )
                )
            )
        }
    }

    override fun createUser(request: CreateUserRequest): CreateUserResult {
        return with (context) {
            request.registerWithAutoName()
            CreateUserResult().withUser(
                makeProxy<CreateUserRequest, User>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateUserRequest::getPath to User::getPath,
                        CreateUserRequest::getUserName to User::getUserName
                    )
                )
            )
        }
    }

    override fun createVirtualMFADevice(request: CreateVirtualMFADeviceRequest): CreateVirtualMFADeviceResult {
        return with (context) {
            request.registerWithAutoName()
            CreateVirtualMFADeviceResult().withVirtualMFADevice(
                makeProxy<CreateVirtualMFADeviceRequest, VirtualMFADevice>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request
                )
            )
        }
    }


}

