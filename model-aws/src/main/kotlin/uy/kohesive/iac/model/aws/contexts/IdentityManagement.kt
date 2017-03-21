package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.utils.DslScope

@DslScope
class IdentityManagementContext(context: IacContext) : BaseIdentityManagementContext(context) {

    fun IdentityManagementContext.addRoleToInstanceProfile(init: AddRoleToInstanceProfileRequest.() -> Unit): Unit {
        identityManagementClient.addRoleToInstanceProfile(AddRoleToInstanceProfileRequest().apply { init() })
    }

    fun IdentityManagementContext.createRole(roleName: String, init: CreateRoleRequest.() -> Unit): Role {
        return identityManagementClient.createRole(CreateRoleRequest().apply {
            this.roleName = roleName
            this.init()
        }).role
    }

    fun IdentityManagementContext.createPolicy(policyName: String, init: CreatePolicyRequest.() -> Unit): Policy {
        return identityManagementClient.createPolicy(CreatePolicyRequest().apply {
            this.policyName = policyName
            this.init()
        }).policy
    }

    fun IdentityManagementContext.attachRolePolicy(init: AttachRolePolicyRequest.() -> Unit): Unit {
        identityManagementClient.attachRolePolicy(AttachRolePolicyRequest().apply { this.init(); })
    }

    fun IdentityManagementContext.createInstanceProfile(instanceProfileName: String, init: CreateInstanceProfileRequest.() -> Unit): InstanceProfile {
        return identityManagementClient.createInstanceProfile(CreateInstanceProfileRequest().apply { 
          this.instanceProfileName = instanceProfileName
          this.init()
        }).instanceProfile
    }

    fun IdentityManagementContext.attachIamRolePolicy(roleResult: CreateRoleResult, policyResult: CreatePolicyResult): Unit {
        attachRolePolicy {
            roleName  = roleResult.role.roleName
            policyArn = policyResult.policy.arn
        }
    }

    fun IdentityManagementContext.attachIamRolePolicy(role: Role, policy: Policy): Unit {
        attachRolePolicy {
            roleName  = role.roleName
            policyArn = policy.arn
        }
    }

    fun IdentityManagementContext.addRoleToInstanceProfile(role: Role, profile: InstanceProfile): Unit {
        addRoleToInstanceProfile {
            roleName            = role.roleName
            instanceProfileName = profile.instanceProfileName
        }
    }

    fun IdentityManagementContext.addRoleToInstanceProfile(roleResult: CreateRoleResult, profile: CreateInstanceProfileResult): Unit {
        addRoleToInstanceProfile {
            roleName            = roleResult.role.roleName
            instanceProfileName = profile.instanceProfile.instanceProfileName
        }
    }
}