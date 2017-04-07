package uy.kohesive.iac.model.aws

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupResult
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationResult
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.CreateTableResult
import com.amazonaws.services.dynamodbv2.model.TableDescription
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult
import com.amazonaws.services.ec2.model.SecurityGroup
import com.amazonaws.services.identitymanagement.model.*
import com.amazonaws.services.iot.model.CreatePolicyResult
import uy.klutter.core.common.mustNotEndWith
import uy.klutter.core.common.mustNotStartWith
import uy.kohesive.iac.model.aws.cloudformation.wait.*
import uy.kohesive.iac.model.aws.helpers.getPolicyNameFromArn
import kotlin.reflect.KClass
import kotlin.reflect.KFunction1
import kotlin.reflect.full.functions

object AutoNaming {

    // TODO: can we automate this?
    private val requestClassesToDefiningPropertyGetters: Map<KClass<out AmazonWebServiceRequest>, KFunction1<*, String>> = mapOf(
        CreateRoleRequest::class                to CreateRoleRequest::getRoleName,
        CreatePolicyRequest::class              to CreatePolicyRequest::getPolicyName,
        CreateUserRequest::class                to CreateUserRequest::getUserName,
        CreateInstanceProfileRequest::class     to CreateInstanceProfileRequest::getInstanceProfileName,
        CreateAutoScalingGroupRequest::class    to CreateAutoScalingGroupRequest::getAutoScalingGroupName,
        CreateLaunchConfigurationRequest::class to CreateLaunchConfigurationRequest::getLaunchConfigurationName,
        AddRoleToInstanceProfileRequest::class  to AddRoleToInstanceProfileRequest::getInstanceProfileName,
        AttachRolePolicyRequest::class          to AttachRolePolicyRequest::getPolicyNameFromArn,
        AttachGroupPolicyRequest::class         to AttachGroupPolicyRequest::getPolicyNameFromArn,
        AttachUserPolicyRequest::class          to AttachUserPolicyRequest::getPolicyNameFromArn,
        CreateSecurityGroupRequest::class       to CreateSecurityGroupRequest::getGroupName,
        CreateTableRequest::class               to CreateTableRequest::getTableName,
        CreateWaitConditionRequest::class       to CreateWaitConditionRequest::getName,
        CreateWaitHandleRequest::class          to CreateWaitHandleRequest::getName,
        AddUserToGroupRequest::class            to AddUserToGroupRequest::getGroupName,
        CreateAccessKeyRequest::class           to CreateAccessKeyRequest::getUserName,
        CreateAccountAliasRequest::class        to CreateAccountAliasRequest::getAccountAlias,
        CreateGroupRequest::class               to CreateGroupRequest::getGroupName,
        CreateLoginProfileRequest::class        to CreateLoginProfileRequest::getUserName
    )

    // TODO: what else can we do to automate this?
    fun getName(request: AmazonWebServiceRequest): String? {
        val autoNameGetter = request::class.simpleName?.takeIf { it.startsWith("Create") }?.let { requestClassName ->
            val entityName = requestClassName.mustNotStartWith("Create").mustNotEndWith("Request")
            request::class.functions.firstOrNull {
                it.name.toLowerCase() == "getname" || it.name.toLowerCase() == ("get${entityName}name").toLowerCase()
            }
        }

        val getter = autoNameGetter ?: requestClassesToDefiningPropertyGetters[request::class]
            ?: throw java.lang.IllegalArgumentException("Unknown request type: ${ request::class.java.simpleName }")

        return (getter as KFunction1<AmazonWebServiceRequest, String>).invoke(request)
    }

}

enum class AwsTypes(val type: String,
                    val requestClass: KClass<out AmazonWebServiceRequest>,
                    val resultClass: KClass<out AmazonWebServiceResult<out ResponseMetadata>>,
                    val stateClass: KClass<out Any>,
                    vararg val relatedClasses: KClass<out Any>) {

    IamRole("AWS::IAM::Role", CreateRoleRequest::class, CreateRoleResult::class, Role::class),
    IamPolicy("AWS::IAM::Policy", CreatePolicyRequest::class, CreatePolicyResult::class, Policy::class, AttachRolePolicyRequest::class),
    IamInstanceProfile("AWS::IAM::InstanceProfile", CreateInstanceProfileRequest::class, CreateInstanceProfileResult::class, InstanceProfile::class, AddRoleToInstanceProfileRequest::class),
    AutoScalingGroup("AWS::AutoScaling::AutoScalingGroup", CreateAutoScalingGroupRequest::class, CreateAutoScalingGroupResult::class, com.amazonaws.services.autoscaling.model.AutoScalingGroup::class),
    LaunchConfiguration("AWS::AutoScaling::LaunchConfiguration", CreateLaunchConfigurationRequest::class, CreateLaunchConfigurationResult::class, com.amazonaws.services.autoscaling.model.LaunchConfiguration::class),
    Ec2SecurityGroup("AWS::EC2::SecurityGroup", CreateSecurityGroupRequest::class, CreateSecurityGroupResult::class, SecurityGroup::class, AuthorizeSecurityGroupIngressRequest::class),
    DynamoDBTable("AWS::DynamoDB::Table", CreateTableRequest::class, CreateTableResult::class, TableDescription::class),
    WaitCondition("AWS::CloudFormation::WaitCondition", CreateWaitConditionRequest::class, CreateWaitConditionResult::class, uy.kohesive.iac.model.aws.cloudformation.wait.WaitCondition::class),
    WaitHandle("AWS::CloudFormation::WaitConditionHandle", CreateWaitHandleRequest::class, CreateWaitHandleResult::class, uy.kohesive.iac.model.aws.cloudformation.wait.WaitConditionHandle::class);

    companion object {
        private val typeStringToEnum = enumValues<AwsTypes>().map { it.type to it }.toMap()
        private val typeClassToEnum  = enumValues<AwsTypes>()
            .map { item -> (item.relatedClasses.toList() + listOf(item.requestClass, item.resultClass, item.stateClass)).map { it to item } }
            .flatten()
            .toMap()

        private val requestClasses: Set<KClass<out AmazonWebServiceRequest>> = enumValues<AwsTypes>().map { it.requestClass }.toSet()

        fun fromString(typeString: String): AwsTypes = typeStringToEnum.get(typeString) ?: throw IllegalArgumentException("type ${typeString} is not a known AWS type")

        fun fromClass(relatedClass: KClass<out Any>): AwsTypes = typeClassToEnum.get(relatedClass) ?: throw IllegalArgumentException("type ${relatedClass.simpleName} is not a known AWS type")

        fun isCreationRequestClass(requestClass: KClass<out AmazonWebServiceRequest>) = requestClasses.contains(requestClass)
    }
}
