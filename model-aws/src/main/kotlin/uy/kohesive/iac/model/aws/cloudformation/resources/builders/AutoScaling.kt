package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.AutoScaling

class LaunchConfigurationPropertiesBuilder : ResourcePropertiesBuilder<CreateLaunchConfigurationRequest> {

    override val requestClazz = CreateLaunchConfigurationRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateLaunchConfigurationRequest).let {
            AutoScaling.LaunchConfiguration(
                ImageId            = request.imageId,
                InstanceType       = request.instanceType,
                SecurityGroups     = request.securityGroups,
                KeyName            = request.keyName,
                IamInstanceProfile = request.iamInstanceProfile,
                UserData           = request.userData
            )
        }
}

class AutoScalingGroupPropertiesBuilder : ResourcePropertiesBuilder<CreateAutoScalingGroupRequest> {
    override val requestClazz = CreateAutoScalingGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAutoScalingGroupRequest).let {
            AutoScaling.AutoScalingGroup(
                AvailabilityZones       = request.availabilityZones,
                LaunchConfigurationName = request.launchConfigurationName,
                DesiredCapacity         = request.desiredCapacity?.toString(),
                MaxSize                 = request.maxSize.toString(),
                MinSize                 = request.minSize.toString(),
                Tags                    = request.tags.map { tag ->
                    AutoScaling.AutoScalingGroup.TagProperty(
                            Key   = tag.key,
                            Value = tag.value,
                            PropagateAtLaunch = tag.isPropagateAtLaunch.toString()
                    )
                }
            )
        }
}
