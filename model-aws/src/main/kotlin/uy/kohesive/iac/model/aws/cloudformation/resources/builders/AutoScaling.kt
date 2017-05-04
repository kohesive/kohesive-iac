package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.autoscaling.model.*
import com.amazonaws.util.DateUtils
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.AutoScaling

class ScheduledActionPropertiesBuilder : ResourcePropertiesBuilder<PutScheduledUpdateGroupActionRequest> {

    override val requestClazz = PutScheduledUpdateGroupActionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutScheduledUpdateGroupActionRequest).let {
            AutoScaling.ScheduledAction(
                AutoScalingGroupName = it.autoScalingGroupName,
                MinSize              = it.minSize?.toString(),
                MaxSize              = it.maxSize?.toString(),
                StartTime            = it.startTime?.let { DateUtils.formatISO8601Date(it) },
                EndTime              = it.endTime?.let { DateUtils.formatISO8601Date(it) },
                DesiredCapacity      = it.desiredCapacity?.toString(),
                Recurrence           = it.recurrence
            )
        }
}

class AutoScalingGroupPropertiesBuilder : ResourcePropertiesBuilder<CreateAutoScalingGroupRequest> {

    override val requestClazz = CreateAutoScalingGroupRequest::class

    // TODO: missing properties - 'MetricsCollection', 'NotificationConfigurations'
    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAutoScalingGroupRequest).let {
            AutoScaling.AutoScalingGroup(
                AvailabilityZones       = request.availabilityZones,
                Cooldown                = request.defaultCooldown?.toString(),
                DesiredCapacity         = request.desiredCapacity?.toString(),
                HealthCheckGracePeriod  = request.healthCheckGracePeriod?.toString(),
                HealthCheckType         = request.healthCheckType,
                InstanceId              = request.instanceId,
                LaunchConfigurationName = request.launchConfigurationName,
                LoadBalancerNames       = request.loadBalancerNames,
                MaxSize                 = request.maxSize.toString(),
                MinSize                 = request.minSize.toString(),
                PlacementGroup          = request.placementGroup,
                TargetGroupARNs         = request.targetGroupARNs,
                TerminationPolicies     = request.terminationPolicies,
                VPCZoneIdentifier       = request.vpcZoneIdentifier?.split(',')?.map(String::trim),
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

class LaunchConfigurationPropertiesBuilder : ResourcePropertiesBuilder<CreateLaunchConfigurationRequest> {

    override val requestClazz = CreateLaunchConfigurationRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateLaunchConfigurationRequest).let {
            AutoScaling.LaunchConfiguration(
                AssociatePublicIpAddress = request.associatePublicIpAddress?.toString(),
                BlockDeviceMappings      = request.blockDeviceMappings.map {
                    AutoScaling.LaunchConfiguration.MappingProperty(
                        DeviceName = it.deviceName,
                        Ebs        = it.ebs?.let {
                            AutoScaling.LaunchConfiguration.MappingProperty.AutoScalingEBSBlockDevice(
                                DeleteOnTermination = it.deleteOnTermination?.toString(),
                                Encrypted  = it.encrypted?.toString(),
                                Iops       = it.iops?.toString(),
                                SnapshotId = it.snapshotId,
                                VolumeSize = it.volumeSize?.toString(),
                                VolumeType = it.volumeType?.toString()
                            )
                        },
                        NoDevice    = it.noDevice?.toString(),
                        VirtualName = it.virtualName
                    )
                },
                ClassicLinkVPCId             = request.classicLinkVPCId,
                ClassicLinkVPCSecurityGroups = request.classicLinkVPCSecurityGroups,
                EbsOptimized                 = request.ebsOptimized?.toString(),
                IamInstanceProfile           = request.iamInstanceProfile,
                ImageId                      = request.imageId,
                InstanceId                   = request.instanceId,
                InstanceType                 = request.instanceType,
                KernelId                     = request.kernelId,
                KeyName                      = request.keyName,
                PlacementTenancy             = request.placementTenancy,
                SecurityGroups               = request.securityGroups,
                SpotPrice                    = request.spotPrice,
                UserData                     = request.userData,
                RamDiskId                    = request.ramdiskId,
                InstanceMonitoring           = request.instanceMonitoring?.enabled?.toString()
            )
        }

}

class AutoScalingLifecycleHookResourcePropertiesBuilder : ResourcePropertiesBuilder<PutLifecycleHookRequest> {

    override val requestClazz = PutLifecycleHookRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutLifecycleHookRequest).let {
            AutoScaling.LifecycleHook(
                AutoScalingGroupName  = request.autoScalingGroupName,
                DefaultResult         = request.defaultResult,
                HeartbeatTimeout      = request.heartbeatTimeout?.toString(),
                LifecycleTransition   = request.lifecycleTransition,
                NotificationMetadata  = request.notificationMetadata,
                NotificationTargetARN = request.notificationTargetARN,
                RoleARN               = request.roleARN
            )
        }

}

class AutoScalingScalingPolicyResourcePropertiesBuilder : ResourcePropertiesBuilder<PutScalingPolicyRequest> {

    override val requestClazz = PutScalingPolicyRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutScalingPolicyRequest).let {
            AutoScaling.ScalingPolicy(
                AdjustmentType          = request.adjustmentType,
                AutoScalingGroupName    = request.autoScalingGroupName,
                Cooldown                = request.cooldown?.toString(),
                EstimatedInstanceWarmup = request.estimatedInstanceWarmup?.toString(),
                MetricAggregationType   = request.metricAggregationType,
                MinAdjustmentMagnitude  = request.minAdjustmentMagnitude?.toString(),
                PolicyType              = request.policyType,
                ScalingAdjustment       = request.scalingAdjustment?.toString(),
                StepAdjustments         = request.stepAdjustments?.map {
                    AutoScaling.ScalingPolicy.StepAdjustmentProperty(
                        MetricIntervalUpperBound = it.metricIntervalUpperBound?.toString(),
                        MetricIntervalLowerBound = it.metricIntervalLowerBound?.toString(),
                        ScalingAdjustment        = it.scalingAdjustment.toString()
                    )
                }
            )
        }

}
