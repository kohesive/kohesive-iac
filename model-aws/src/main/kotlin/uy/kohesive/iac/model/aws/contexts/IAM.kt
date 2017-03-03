package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope


interface IamRoleIdentifiable : KohesiveIdentifiable {
}

interface IamRoleEnabled : IamRoleIdentifiable {
    val iamClient: AmazonIdentityManagement
    val iamContext: IamContext
    fun <T> withIamContext(init: IamContext.(AmazonIdentityManagement) -> T): T = iamContext.init(iamClient)
}

@DslScope
class IamContext(private val context: IacContext) : IamRoleEnabled by context {

    fun IamContext.addRoleToInstanceProfile(init: AddRoleToInstanceProfileRequest.() -> Unit): Unit {
        iamClient.addRoleToInstanceProfile(AddRoleToInstanceProfileRequest().apply { init() })
    }

    fun IamContext.createRole(roleName: String, init: CreateRoleRequest.() -> Unit): Role {
        return iamClient.createRole(CreateRoleRequest().apply {
            this.roleName = roleName
            this.init()
        }).role
    }

    fun IamContext.createPolicy(policyName: String, init: CreatePolicyRequest.() -> Unit): Policy {
        return iamClient.createPolicy(CreatePolicyRequest().apply {
            this.policyName = policyName
            this.init()
        }).policy
    }

    fun IamContext.attachRolePolicy(init: AttachRolePolicyRequest.() -> Unit): Unit {
        iamClient.attachRolePolicy(AttachRolePolicyRequest().apply { this.init(); })
    }

    fun IamContext.createInstanceProfile(instanceProfileName: String, init: CreateInstanceProfileRequest.() -> Unit): InstanceProfile {
        return iamClient.createInstanceProfile(CreateInstanceProfileRequest().apply { 
          this.instanceProfileName = instanceProfileName
          this.init()
        }).instanceProfile
    }

    fun IamContext.attachIamRolePolicy(roleResult: CreateRoleResult, policyResult: CreatePolicyResult): Unit {
        attachRolePolicy {
            roleName  = roleResult.role.roleName
            policyArn = policyResult.policy.arn
        }
    }

    fun IamContext.attachIamRolePolicy(role: Role, policy: Policy): Unit {
        attachRolePolicy {
            roleName  = role.roleName
            policyArn = policy.arn
        }
    }

    fun IamContext.addRoleToInstanceProfile(role: Role, profile: InstanceProfile): Unit {
        addRoleToInstanceProfile {
            roleName            = role.roleName
            instanceProfileName = profile.instanceProfileName
        }
    }

    fun IamContext.addRoleToInstanceProfile(roleResult: CreateRoleResult, profile: CreateInstanceProfileResult): Unit {
        addRoleToInstanceProfile {
            roleName            = roleResult.role.roleName
            instanceProfileName = profile.instanceProfile.instanceProfileName
        }
    }
}