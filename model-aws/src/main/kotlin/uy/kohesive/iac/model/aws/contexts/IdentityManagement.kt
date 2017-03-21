package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.utils.DslScope

@DslScope
class IdentityManagementContext(context: IacContext) : BaseIdentityManagementContext(context) {

    fun addRoleToInstanceProfile(init: AddRoleToInstanceProfileRequest.() -> Unit): Unit {
        identityManagementClient.addRoleToInstanceProfile(AddRoleToInstanceProfileRequest().apply { init() })
    }

    fun attachRolePolicy(init: AttachRolePolicyRequest.() -> Unit): Unit {
        identityManagementClient.attachRolePolicy(AttachRolePolicyRequest().apply { this.init(); })
    }

    fun attachIamRolePolicy(roleResult: CreateRoleResult, policyResult: CreatePolicyResult): Unit {
        attachRolePolicy {
            roleName  = roleResult.role.roleName
            policyArn = policyResult.policy.arn
        }
    }

    fun attachIamRolePolicy(role: Role, policy: Policy): Unit {
        attachRolePolicy {
            roleName  = role.roleName
            policyArn = policy.arn
        }
    }

    fun addRoleToInstanceProfile(role: Role, profile: InstanceProfile): Unit {
        addRoleToInstanceProfile {
            roleName            = role.roleName
            instanceProfileName = profile.instanceProfileName
        }
    }

    fun addRoleToInstanceProfile(roleResult: CreateRoleResult, profile: CreateInstanceProfileResult): Unit {
        addRoleToInstanceProfile {
            roleName            = roleResult.role.roleName
            instanceProfileName = profile.instanceProfile.instanceProfileName
        }
    }
}