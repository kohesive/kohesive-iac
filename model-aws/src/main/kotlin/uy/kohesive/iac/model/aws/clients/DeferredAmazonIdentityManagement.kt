package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

class DeferredAmazonIdentityManagement(context: IacContext) : BaseDeferredAmazonIdentityManagement(context) {

    override fun attachRolePolicy(request: AttachRolePolicyRequest): AttachRolePolicyResult = with (context) {
        request.registerWithAutoName()
        AttachRolePolicyResult().registerWithSameNameAs(request)
    }

    override fun addRoleToInstanceProfile(request: AddRoleToInstanceProfileRequest): AddRoleToInstanceProfileResult {
        return with (context) {
            request.registerWithAutoName()
            AddRoleToInstanceProfileResult().registerWithSameNameAs(request)
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
            request.registerWithAutoName()
            CreateRoleResult().withRole(
                makeProxy<CreateRoleRequest, Role>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateRoleRequest::getAssumeRolePolicyDocument to Role::getAssumeRolePolicyDocument,
                        CreateRoleRequest::getPath     to Role::getPath,
                        CreateRoleRequest::getRoleName to Role::getRoleName
                    )
                )
            )
        }
    }

    // TODO: does this look right?
    override fun createPolicy(request: CreatePolicyRequest): CreatePolicyResult {
        return with (context) {
            request.registerWithAutoName()
            CreatePolicyResult().withPolicy(
                makeProxy<CreatePolicyRequest, Policy>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreatePolicyRequest::getDescription to Policy::getDescription,
                        CreatePolicyRequest::getPath        to Policy::getPath,
                        CreatePolicyRequest::getPolicyName  to Policy::getPolicyName
                    )
                )
            )
        }
    }
}