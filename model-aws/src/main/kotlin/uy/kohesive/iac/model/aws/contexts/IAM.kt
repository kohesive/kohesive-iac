package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope


interface IamRoleIdentifiable : KohesiveIdentifiable {
    fun CreateRoleRequest.withKohesiveIdFromName(): CreateRoleRequest = apply {
        withKohesiveId(this.roleName)
    }

    fun CreatePolicyRequest.withKohesiveIdFromName(): CreatePolicyRequest = apply {
        withKohesiveId(this.policyName)
    }

    fun CreateInstanceProfileRequest.withKohesiveIdFromName(): CreateInstanceProfileRequest = apply {
        withKohesiveId(this.instanceProfileName)
    }
}

interface IamRoleEnabled : IamRoleIdentifiable {
    val iamClient: AmazonIdentityManagement
    val iamContext: IamContext
    fun <T> withIamContext(init: IamContext.(AmazonIdentityManagement) -> T): T = iamContext.init(iamClient)
}

@DslScope
class IamContext(private val context: IacContext) : IamRoleEnabled by context {
    fun IamContext.createRole(init: CreateRoleRequest.() -> Unit): CreateRoleResult {
        return iamClient.createRole(CreateRoleRequest().apply { init(); withKohesiveIdFromName() })
    }

    fun IamContext.createPolicy(init: CreatePolicyRequest.() -> Unit): CreatePolicyResult {
        return iamClient.createPolicy(CreatePolicyRequest().apply { this.init(); withKohesiveIdFromName() })
    }

    fun IamContext.attachRolePolicy(init: AttachRolePolicyRequest.() -> Unit): AttachRolePolicyResult {
        return iamClient.attachRolePolicy(AttachRolePolicyRequest().apply { this.init(); withKohesiveId(this.roleName + " => " + this.policyArn) })
    }

    fun IamContext.attachIamRolePolicy(roleResult: CreateRoleResult, policyResult: CreatePolicyResult): AttachRolePolicyResult {
        return attachRolePolicy {
            roleName = roleResult.role.roleName
            policyArn = policyResult.policy.arn
        }
    }

    fun IamContext.attachIamRolePolicy(role: Role, policy: Policy): AttachRolePolicyResult {
        return attachRolePolicy {
            roleName = role.roleName
            policyArn = policy.arn
        }
    }

    fun createInstanceProfile(init: CreateInstanceProfileRequest.()->Unit): CreateInstanceProfileResult {
        return iamClient.createInstanceProfile(CreateInstanceProfileRequest().apply { this.init(); withKohesiveIdFromName() })
    }
}