package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable

interface IdentityManagementIdentifiable : KohesiveIdentifiable {

}

interface IdentityManagementEnabled : IdentityManagementIdentifiable {
    val identityManagementClient: AmazonIdentityManagement
    val identityManagementContext: IdentityManagementContext
    fun <T> withIdentityManagementContext(init: IdentityManagementContext.(AmazonIdentityManagement) -> T): T = identityManagementContext.init(identityManagementClient)
}

open class BaseIdentityManagementContext(protected val context: IacContext) : IdentityManagementEnabled by context {

    fun createGroup(groupName: String, init: CreateGroupRequest.() -> Unit): Group {
        return identityManagementClient.createGroup(CreateGroupRequest().apply {
            withGroupName(groupName)
            init()
        }).group
    }

    fun createInstanceProfile(instanceProfileName: String, init: CreateInstanceProfileRequest.() -> Unit): InstanceProfile {
        return identityManagementClient.createInstanceProfile(CreateInstanceProfileRequest().apply {
            withInstanceProfileName(instanceProfileName)
            init()
        }).instanceProfile
    }

    fun createPolicy(policyName: String, init: CreatePolicyRequest.() -> Unit): Policy {
        return identityManagementClient.createPolicy(CreatePolicyRequest().apply {
            withPolicyName(policyName)
            init()
        }).policy
    }

    fun createRole(roleName: String, init: CreateRoleRequest.() -> Unit): Role {
        return identityManagementClient.createRole(CreateRoleRequest().apply {
            withRoleName(roleName)
            init()
        }).role
    }

    fun createUser(userName: String, init: CreateUserRequest.() -> Unit): User {
        return identityManagementClient.createUser(CreateUserRequest().apply {
            withUserName(userName)
            init()
        }).user
    }

    fun createVirtualMFADevice(virtualMFADeviceName: String, init: CreateVirtualMFADeviceRequest.() -> Unit): VirtualMFADevice {
        return identityManagementClient.createVirtualMFADevice(CreateVirtualMFADeviceRequest().apply {
            withVirtualMFADeviceName(virtualMFADeviceName)
            init()
        }).virtualMFADevice
    }


}

