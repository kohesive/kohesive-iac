package uy.kohesive.iac.model.aws

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupResult
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationResult
import com.amazonaws.services.identitymanagement.model.*
import com.amazonaws.services.iot.model.CreatePolicyResult
import kotlin.reflect.KClass

enum class AwsTypes(val type: String,
                    val requestClass: KClass<out AmazonWebServiceRequest>,
                    val resultClass: KClass<out AmazonWebServiceResult<out ResponseMetadata>>,
                    val stateClass: KClass<out Any>,
                    vararg val relatedClasses: KClass<out Any>) {

    IamRole("AWS::IAM::Role", CreateRoleRequest::class, CreateRoleResult::class, Role::class),
    IamPolicy("AWS::IAM::Policy", CreatePolicyRequest::class, CreatePolicyResult::class, Policy::class),
    IamInstanceProfile("AWS::IAM::InstanceProfile", CreateInstanceProfileRequest::class, CreateInstanceProfileResult::class, InstanceProfile::class),
    AutoScalingGroup("AWS::AutoScaling::AutoScalingGroup", CreateAutoScalingGroupRequest::class, CreateAutoScalingGroupResult::class, com.amazonaws.services.autoscaling.model.AutoScalingGroup::class),
    LaunchConfiguration("AWS::AutoScaling::LaunchConfiguration", CreateLaunchConfigurationRequest::class, CreateLaunchConfigurationResult::class, com.amazonaws.services.autoscaling.model.LaunchConfiguration::class);

    companion object {
        private val typeStringToEnum = enumValues<AwsTypes>().map { it.type to it }.toMap()
        private val typeClassToEnum  = enumValues<AwsTypes>()
                .map { item -> (item.relatedClasses.toList() + listOf(item.requestClass, item.resultClass, item.stateClass)).map { it to item } }
                .flatten()
                .toMap()

        fun fromString(typeString: String): AwsTypes = typeStringToEnum.get(typeString) ?: throw IllegalArgumentException("type ${typeString} is not a known AWS type")
        fun fromClass(relatedClass: KClass<out Any>): AwsTypes = typeClassToEnum.get(relatedClass) ?: throw IllegalArgumentException("type ${relatedClass.simpleName} is not a known AWS type")
    }
}
