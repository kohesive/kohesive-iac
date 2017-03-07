package uy.kohesive.iac.model.aws.cloudformation.resources

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder

class LaunchConfigurationPropertiesBuilder : ResourcePropertiesBuilder<CreateLaunchConfigurationRequest> {

    override val requestClazz = CreateLaunchConfigurationRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateLaunchConfigurationRequest).let {
            AutoScalingLaunchConfigurationProperties(
                ImageId            = request.imageId,
                InstanceType       = request.instanceType,
                SecurityGroups     = request.securityGroups,
                KeyName            = request.keyName,
                IamInstanceProfile = request.iamInstanceProfile
            )
        }
}

class AutoScalingGroupPropertiesBuilder : ResourcePropertiesBuilder<CreateAutoScalingGroupRequest> {
    override val requestClazz = CreateAutoScalingGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAutoScalingGroupRequest).let {
            AutoScalingGroupProperties(
                AvailabilityZones       = request.availabilityZones,
                LaunchConfigurationName = request.launchConfigurationName,
                DesiredCapacity         = request.desiredCapacity?.toString(),
                MaxSize                 = request.maxSize?.toString(),
                MinSize                 = request.minSize?.toString(),
                Tags                    = request.tags.map { tag ->
                    AutoScalingGroupTag(
                        Key   = tag.key,
                        Value = tag.value,
                        PropagateAtLaunch = tag.isPropagateAtLaunch?.toString()
                    )
                }
            )
        }
}

data class AutoScalingLaunchConfigurationProperties(
    val ImageId: String?,
    val InstanceType: String?,
    val SecurityGroups: List<String>?,
    val KeyName: String?,
    val IamInstanceProfile: String?
    // TODO: UserData
) : ResourceProperties

data class AutoScalingGroupProperties(
    val AvailabilityZones: List<String>?,
    val LaunchConfigurationName: String?,
    val MinSize: String?,
    val MaxSize: String?,
    val DesiredCapacity: String?,
    val Tags: List<AutoScalingGroupTag>?
) : ResourceProperties

data class AutoScalingGroupTag(
    val Key: String?,
    val Value: String?,
    val PropagateAtLaunch: String?
)