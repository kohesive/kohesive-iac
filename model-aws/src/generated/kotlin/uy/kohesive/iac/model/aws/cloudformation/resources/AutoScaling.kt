package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes
import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties

@CloudFormationTypes
object AutoScaling {

    @CloudFormationType("AWS::AutoScaling::AutoScalingGroup")
    data class AutoScalingGroup(
        val AvailabilityZones: List<String>? = null,
        val Cooldown: String? = null,
        val DesiredCapacity: String? = null,
        val HealthCheckGracePeriod: String? = null,
        val HealthCheckType: String? = null,
        val InstanceId: String? = null,
        val LaunchConfigurationName: String? = null,
        val LoadBalancerNames: List<String>? = null,
        val MaxSize: String,
        val MetricsCollection: List<AutoScaling.AutoScalingGroup.MetricsCollectionProperty>? = null,
        val MinSize: String,
        val NotificationConfigurations: List<AutoScaling.AutoScalingGroup.NotificationConfigurationProperty>? = null,
        val PlacementGroup: String? = null,
        val Tags: List<AutoScaling.AutoScalingGroup.TagProperty>? = null,
        val TargetGroupARNs: List<String>? = null,
        val TerminationPolicies: List<String>? = null,
        val VPCZoneIdentifier: List<String>? = null
    ) : ResourceProperties {

        data class MetricsCollectionProperty(
            val Granularity: String,
            val Metrics: List<String>? = null
        ) 


        data class NotificationConfigurationProperty(
            val NotificationTypes: List<String>,
            val TopicARN: String
        ) 


        data class TagProperty(
            val Key: String,
            val Value: String,
            val PropagateAtLaunch: String
        ) 

    }

    @CloudFormationType("AWS::AutoScaling::LaunchConfiguration")
    data class LaunchConfiguration(
        val AssociatePublicIpAddress: String? = null,
        val BlockDeviceMappings: List<AutoScaling.LaunchConfiguration.MappingProperty>? = null,
        val ClassicLinkVPCId: String? = null,
        val ClassicLinkVPCSecurityGroups: List<String>? = null,
        val EbsOptimized: String? = null,
        val IamInstanceProfile: String? = null,
        val ImageId: String,
        val InstanceId: String? = null,
        val InstanceMonitoring: String? = null,
        val InstanceType: String,
        val KernelId: String? = null,
        val KeyName: String? = null,
        val PlacementTenancy: String? = null,
        val RamDiskId: String? = null,
        val SecurityGroups: List<String>? = null,
        val SpotPrice: String? = null,
        val UserData: String? = null
    ) : ResourceProperties {

        data class MappingProperty(
            val DeviceName: String,
            val Ebs: LaunchConfiguration.MappingProperty.AutoScalingEBSBlockDevice? = null,
            val NoDevice: String? = null,
            val VirtualName: String? = null
        ) {

            data class AutoScalingEBSBlockDevice(
                val DeleteOnTermination: String? = null,
                val Encrypted: String? = null,
                val Iops: String? = null,
                val SnapshotId: String? = null,
                val VolumeSize: String? = null,
                val VolumeType: String? = null
            ) 

        }

    }

    @CloudFormationType("AWS::AutoScaling::LifecycleHook")
    data class LifecycleHook(
        val AutoScalingGroupName: String,
        val DefaultResult: String? = null,
        val HeartbeatTimeout: String? = null,
        val LifecycleTransition: String,
        val NotificationMetadata: String? = null,
        val NotificationTargetARN: String,
        val RoleARN: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::AutoScaling::ScalingPolicy")
    data class ScalingPolicy(
        val AdjustmentType: String,
        val AutoScalingGroupName: String,
        val Cooldown: String? = null,
        val EstimatedInstanceWarmup: String? = null,
        val MetricAggregationType: String? = null,
        val MinAdjustmentMagnitude: String? = null,
        val PolicyType: String? = null,
        val ScalingAdjustment: String? = null,
        val StepAdjustments: List<AutoScaling.ScalingPolicy.StepAdjustmentProperty>? = null
    ) : ResourceProperties {

        data class StepAdjustmentProperty(
            val MetricIntervalLowerBound: String? = null,
            val MetricIntervalUpperBound: String? = null,
            val ScalingAdjustment: String
        ) 

    }

    @CloudFormationType("AWS::AutoScaling::ScheduledAction")
    data class ScheduledAction(
        val AutoScalingGroupName: String,
        val DesiredCapacity: String? = null,
        val EndTime: String? = null,
        val MaxSize: String? = null,
        val MinSize: String? = null,
        val Recurrence: String? = null,
        val StartTime: String? = null
    ) : ResourceProperties 


}