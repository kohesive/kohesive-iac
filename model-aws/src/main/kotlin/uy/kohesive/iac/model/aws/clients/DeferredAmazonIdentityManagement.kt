package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.identitymanagement.model.AddRoleToInstanceProfileRequest
import com.amazonaws.services.identitymanagement.model.AddRoleToInstanceProfileResult
import com.amazonaws.services.identitymanagement.model.AttachRolePolicyRequest
import com.amazonaws.services.identitymanagement.model.AttachRolePolicyResult
import uy.kohesive.iac.model.aws.IacContext

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

}