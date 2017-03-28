package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes
import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties

@CloudFormationTypes
object IAM {

    @CloudFormationType("AWS::IAM::AccessKey")
    data class AccessKey(
        val Serial: String? = null,
        val Status: String? = null,
        val UserName: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::IAM::Group")
    data class Group(
        val GroupName: String? = null,
        val ManagedPolicyArns: List<String>? = null,
        val Path: String? = null,
        val Policies: List<IAM.Role.PolicyProperty>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::IAM::InstanceProfile")
    data class InstanceProfile(
        val Path: String? = null,
        val Roles: List<String>
    ) : ResourceProperties 

    @CloudFormationType("AWS::IAM::ManagedPolicy")
    data class ManagedPolicy(
        val Description: String? = null,
        val Groups: List<String>? = null,
        val Path: String? = null,
        val PolicyDocument: Any,
        val Roles: List<String>? = null,
        val Users: List<String>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::IAM::Policy")
    data class Policy(
        val Groups: List<String>? = null,
        val PolicyDocument: Any,
        val PolicyName: String,
        val Roles: List<String>? = null,
        val Users: List<String>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::IAM::Role")
    data class Role(
        val AssumeRolePolicyDocument: Any,
        val ManagedPolicyArns: List<String>? = null,
        val Path: String? = null,
        val Policies: List<IAM.Role.PolicyProperty>? = null,
        val RoleName: String? = null
    ) : ResourceProperties {

        data class PolicyProperty(
            val PolicyDocument: Any,
            val PolicyName: String
        ) 

    }

    @CloudFormationType("AWS::IAM::User")
    data class User(
        val Groups: List<String>? = null,
        val LoginProfile: User.LoginProfileProperty? = null,
        val ManagedPolicyArns: List<String>? = null,
        val Path: String? = null,
        val Policies: List<IAM.Role.PolicyProperty>? = null,
        val UserName: String? = null
    ) : ResourceProperties {

        data class LoginProfileProperty(
            val Password: String,
            val PasswordResetRequired: String? = null
        ) 

    }

    @CloudFormationType("AWS::IAM::UserToGroupAddition")
    data class UserToGroupAddition(
        val GroupName: String,
        val Users: List<String>
    ) : ResourceProperties 


}