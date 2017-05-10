package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes
import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties

@CloudFormationTypes
object OpsWorks {

    @CloudFormationType("AWS::OpsWorks::App")
    data class App(
        val AppSource: Stack.SourceProperty? = null,
        val Attributes: Map<String, String>? = null,
        val Description: String? = null,
        val DataSources: List<OpsWorks.App.DataSourceProperty>? = null,
        val Domains: List<String>? = null,
        val EnableSsl: String? = null,
        val Environment: List<OpsWorks.App.EnvironmentProperty>? = null,
        val Name: String,
        val Shortname: String? = null,
        val SslConfiguration: App.SslConfigurationProperty? = null,
        val StackId: String,
        val Type: String
    ) : ResourceProperties {

        data class DataSourceProperty(
            val Arn: String? = null,
            val DatabaseName: String? = null,
            val Type: String? = null
        ) 


        data class EnvironmentProperty(
            val Key: String,
            val Secure: String? = null,
            val Value: String
        ) 


        data class SslConfigurationProperty(
            val Certificate: String,
            val Chain: String? = null,
            val PrivateKey: String
        ) 

    }

    @CloudFormationType("AWS::OpsWorks::ElasticLoadBalancerAttachment")
    data class ElasticLoadBalancerAttachment(
        val ElasticLoadBalancerName: String,
        val LayerId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::OpsWorks::Instance")
    data class Instance(
        val AgentVersion: String? = null,
        val AmiId: String? = null,
        val Architecture: String? = null,
        val AutoScalingType: String? = null,
        val AvailabilityZone: String? = null,
        val BlockDeviceMappings: List<OpsWorks.Instance.BlockDeviceMappingProperty>? = null,
        val EbsOptimized: String? = null,
        val ElasticIps: List<String>? = null,
        val Hostname: String? = null,
        val InstallUpdatesOnBoot: String? = null,
        val InstanceType: String,
        val LayerIds: List<String>,
        val Os: String? = null,
        val RootDeviceType: String? = null,
        val SshKeyName: String? = null,
        val StackId: String,
        val SubnetId: String? = null,
        val Tenancy: String? = null,
        val TimeBasedAutoScaling: Instance.TimeBasedAutoScalingProperty? = null,
        val VirtualizationType: String? = null,
        val Volumes: List<String>? = null
    ) : ResourceProperties {

        data class BlockDeviceMappingProperty(
            val DeviceName: String? = null,
            val Ebs: Instance.BlockDeviceMappingProperty.EbsBlockDeviceProperty? = null,
            val NoDevice: String? = null,
            val VirtualName: String? = null
        ) {

            data class EbsBlockDeviceProperty(
                val DeleteOnTermination: String? = null,
                val Iops: String? = null,
                val SnapshotId: String? = null,
                val VolumeSize: String? = null,
                val VolumeType: String? = null
            ) 

        }


        data class TimeBasedAutoScalingProperty(
            val Friday: Map<String, String>? = null,
            val Monday: Map<String, String>? = null,
            val Saturday: Map<String, String>? = null,
            val Sunday: Map<String, String>? = null,
            val Thursday: Map<String, String>? = null,
            val Tuesday: Map<String, String>? = null,
            val Wednesday: Map<String, String>? = null
        ) 

    }

    @CloudFormationType("AWS::OpsWorks::Layer")
    data class Layer(
        val Attributes: Map<String, String>? = null,
        val AutoAssignElasticIps: String,
        val AutoAssignPublicIps: String,
        val CustomInstanceProfileArn: String? = null,
        val CustomJson: Any? = null,
        val CustomRecipes: Layer.RecipeProperty? = null,
        val CustomSecurityGroupIds: List<String>? = null,
        val EnableAutoHealing: String,
        val InstallUpdatesOnBoot: String? = null,
        val LifecycleEventConfiguration: Layer.LifeCycleConfigurationProperty? = null,
        val LoadBasedAutoScaling: Layer.LoadBasedAutoScalingProperty? = null,
        val Name: String,
        val Packages: List<String>? = null,
        val Shortname: String,
        val StackId: String,
        val Type: String,
        val VolumeConfigurations: List<OpsWorks.Layer.VolumeConfigurationProperty>? = null
    ) : ResourceProperties {

        data class RecipeProperty(
            val Configure: List<String>? = null,
            val Deploy: List<String>? = null,
            val Setup: List<String>? = null,
            val Shutdown: List<String>? = null,
            val Undeploy: List<String>? = null
        ) 


        data class LifeCycleConfigurationProperty(
            val ShutdownEventConfiguration: Layer.LifeCycleConfigurationProperty.ShutdownEventConfigurationProperty? = null
        ) {

            data class ShutdownEventConfigurationProperty(
                val DelayUntilElbConnectionsDrained: String? = null,
                val ExecutionTimeout: String? = null
            ) 

        }


        data class LoadBasedAutoScalingProperty(
            val DownScaling: Layer.LoadBasedAutoScalingProperty.AutoScalingThresholdProperty? = null,
            val Enable: String? = null,
            val UpScaling: Layer.LoadBasedAutoScalingProperty.AutoScalingThresholdProperty? = null
        ) {

            data class AutoScalingThresholdProperty(
                val CpuThreshold: String? = null,
                val IgnoreMetricsTime: String? = null,
                val InstanceCount: String? = null,
                val LoadThreshold: String? = null,
                val MemoryThreshold: String? = null,
                val ThresholdsWaitTime: String? = null
            ) 

        }


        data class VolumeConfigurationProperty(
            val Iops: String? = null,
            val MountPoint: String,
            val NumberOfDisks: String,
            val RaidLevel: String? = null,
            val Size: String,
            val VolumeType: String? = null
        ) 

    }

    @CloudFormationType("AWS::OpsWorks::Stack")
    data class Stack(
        val AgentVersion: String? = null,
        val Attributes: Map<String, String>? = null,
        val ChefConfiguration: Stack.ChefConfigurationProperty? = null,
        val CloneAppIds: List<String>? = null,
        val ClonePermissions: String? = null,
        val ConfigurationManager: Stack.StackConfigurationManagerProperty? = null,
        val CustomCookbooksSource: Stack.SourceProperty? = null,
        val CustomJson: Any? = null,
        val DefaultAvailabilityZone: String? = null,
        val DefaultInstanceProfileArn: String,
        val DefaultOs: String? = null,
        val DefaultRootDeviceType: String? = null,
        val DefaultSshKeyName: String? = null,
        val DefaultSubnetId: String? = null,
        val EcsClusterArn: String? = null,
        val ElasticIps: List<OpsWorks.Stack.ElasticIpProperty>? = null,
        val HostnameTheme: String? = null,
        val Name: String,
        val RdsDbInstances: List<OpsWorks.Stack.RdsDbInstanceProperty>? = null,
        val ServiceRoleArn: String,
        val SourceStackId: String? = null,
        val UseCustomCookbooks: String? = null,
        val UseOpsworksSecurityGroups: String? = null,
        val VpcId: String? = null
    ) : ResourceProperties {

        data class SourceProperty(
            val Password: String? = null,
            val Revision: String? = null,
            val SshKey: String? = null,
            val Type: String? = null,
            val Url: String? = null,
            val Username: String? = null
        ) 


        data class ChefConfigurationProperty(
            val BerkshelfVersion: String? = null,
            val ManageBerkshelf: String? = null
        ) 


        data class StackConfigurationManagerProperty(
            val Name: String? = null,
            val Version: String? = null
        ) 


        data class ElasticIpProperty(
            val Ip: String,
            val Name: String? = null
        ) 


        data class RdsDbInstanceProperty(
            val DbPassword: String,
            val DbUser: String,
            val RdsDbInstanceArn: String
        ) 

    }

    @CloudFormationType("AWS::OpsWorks::UserProfile")
    data class UserProfile(
        val AllowSelfManagement: String? = null,
        val IamUserArn: String,
        val SshPublicKey: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::OpsWorks::Volume")
    data class Volume(
        val Ec2VolumeId: String,
        val MountPoint: String? = null,
        val Name: String? = null,
        val StackId: String
    ) : ResourceProperties 


}