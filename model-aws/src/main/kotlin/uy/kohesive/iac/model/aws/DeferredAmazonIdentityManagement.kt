package uy.kohesive.iac.model.aws

import com.amazonaws.services.identitymanagement.AbstractAmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.proxy.makeProxy

class DeferredAmazonIdentityManagement(val context: IacContext) : AbstractAmazonIdentityManagement(), AmazonIdentityManagement {

    override fun attachRolePolicy(request: AttachRolePolicyRequest): AttachRolePolicyResult {
        // TODO: do we need to do anything there? I think not — we've registered the request already
        return AttachRolePolicyResult()
    }

    override fun addRoleToInstanceProfile(request: AddRoleToInstanceProfileRequest): AddRoleToInstanceProfileResult {
        // TODO: do we need to do anything there? I think not — we've registered the request already
        return AddRoleToInstanceProfileResult()
    }

    override fun createInstanceProfile(request: CreateInstanceProfileRequest): CreateInstanceProfileResult {
        return with (context) {
            CreateInstanceProfileResult().withInstanceProfile(
                makeProxy<CreateInstanceProfileRequest, InstanceProfile>(
                    context       = this@with,
                    id            = getId(request) ?: throw IllegalStateException(),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateInstanceProfileRequest::getInstanceProfileName to InstanceProfile::getInstanceProfileName,
                        CreateInstanceProfileRequest::getPath to InstanceProfile::getPath
                    )
                )
            )
        }
    }

    // TODO: does this look right?
    override fun createRole(request: CreateRoleRequest): CreateRoleResult {
        return with (context) {
            CreateRoleResult().withRole(
                makeProxy<CreateRoleRequest, Role>(
                    context       = this@with,
                    id            = getId(request) ?: throw IllegalStateException(),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateRoleRequest::getAssumeRolePolicyDocument to Role::getAssumeRolePolicyDocument,
                        CreateRoleRequest::getPath to Role::getPath,
                        CreateRoleRequest::getRoleName to Role::getRoleName
                    )
                )
            )
        }
    }

    // TODO: does this look right?
    override fun createPolicy(request: CreatePolicyRequest): CreatePolicyResult {
        return with (context) {
            CreatePolicyResult().withPolicy(
                makeProxy<CreatePolicyRequest, Policy>(
                    context       = this@with,
                    id            = getId(request) ?: throw IllegalStateException(),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreatePolicyRequest::getDescription to Policy::getDescription,
                        CreatePolicyRequest::getPath to Policy::getPath,
                        CreatePolicyRequest::getPolicyName to Policy::getPolicyName
                    )
                )
            )
        }
    }
}