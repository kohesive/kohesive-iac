package uy.kohesive.iac.model.aws

import com.amazonaws.services.identitymanagement.AbstractAmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.proxy.makeProxy

class DeferredAmazonIdentityManagement(val context: IacContext) : AbstractAmazonIdentityManagement(), AmazonIdentityManagement {

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