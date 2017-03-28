package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object EMR {

    @CloudFormationType("AWS::EMR::Cluster")
    data class Cluster(
        val AdditionalInfo: Any? = null,
        val Applications: List<EMR.Cluster.ApplicationProperty>? = null,
        val BootstrapActions: List<EMR.Cluster.BootstrapActionConfigProperty>? = null,
        val Configurations: List<EMR.Cluster.ConfigurationProperty>? = null,
        val Instances: Cluster.JobFlowInstancesConfigProperty,
        val JobFlowRole: String,
        val LogUri: String? = null,
        val Name: String,
        val ReleaseLabel: String? = null,
        val ServiceRole: String,
        val Tags: CloudFormation.ResourceTag? = null,
        val VisibleToAllUsers: String? = null
    ) : ResourceProperties {

        data class ApplicationProperty(
            val AdditionalInfo: Map<String, String>? = null,
            val Args: List<String>? = null,
            val Name: String? = null,
            val Version: String? = null
        ) 


        data class BootstrapActionConfigProperty(
            val Name: String,
            val ScriptBootstrapAction: Cluster.BootstrapActionConfigProperty.ScriptBootstrapActionConfigProperty
        ) {

            data class ScriptBootstrapActionConfigProperty(
                val Args: List<String>? = null,
                val Path: String
            ) 

        }


        data class ConfigurationProperty(
            val Classification: String? = null,
            val ConfigurationProperties: Map<String, String>? = null,
            val Configurations: List<EMR.Cluster.ConfigurationProperty>? = null
        ) 


        data class JobFlowInstancesConfigProperty(
            val AdditionalMasterSecurityGroups: List<String>? = null,
            val AdditionalSlaveSecurityGroups: List<String>? = null,
            val CoreInstanceGroup: Cluster.JobFlowInstancesConfigProperty.InstanceGroupConfigProperty,
            val Ec2KeyName: String? = null,
            val Ec2SubnetId: String? = null,
            val EmrManagedMasterSecurityGroup: String? = null,
            val EmrManagedSlaveSecurityGroup: String? = null,
            val HadoopVersion: String? = null,
            val MasterInstanceGroup: Cluster.JobFlowInstancesConfigProperty.InstanceGroupConfigProperty,
            val Placement: Cluster.JobFlowInstancesConfigProperty.PlacementProperty? = null,
            val ServiceAccessSecurityGroup: String? = null,
            val TerminationProtected: String? = null
        ) {

            data class InstanceGroupConfigProperty(
                val BidPrice: String? = null,
                val Configurations: List<EMR.Cluster.ConfigurationProperty>? = null,
                val EbsConfiguration: Cluster.JobFlowInstancesConfigProperty.InstanceGroupConfigProperty.EbsConfigurationProperty? = null,
                val InstanceCount: String,
                val InstanceType: String,
                val Market: String? = null,
                val Name: String? = null
            ) {

                data class EbsConfigurationProperty(
                    val EbsBlockDeviceConfigs: List<EMR.Cluster.JobFlowInstancesConfigProperty.InstanceGroupConfigProperty.EbsConfigurationProperty.EbsBlockDeviceConfigProperty>? = null,
                    val EbsOptimized: String? = null
                ) {

                    data class EbsBlockDeviceConfigProperty(
                        val VolumeSpecification: Cluster.JobFlowInstancesConfigProperty.InstanceGroupConfigProperty.EbsConfigurationProperty.VolumeSpecificationProperty,
                        val VolumesPerInstance: String? = null
                    ) 


                    data class VolumeSpecificationProperty(
                        val Iops: String? = null,
                        val SizeInGB: String,
                        val VolumeType: String
                    ) 

                }

            }


            data class PlacementProperty(
                val AvailabilityZone: String
            ) 

        }

    }

    @CloudFormationType("AWS::EMR::InstanceGroupConfig")
    data class InstanceGroupConfig(
        val BidPrice: String? = null,
        val Configurations: List<EMR.Cluster.ConfigurationProperty>? = null,
        val EbsConfiguration: Cluster.JobFlowInstancesConfigProperty.InstanceGroupConfigProperty.EbsConfigurationProperty? = null,
        val InstanceCount: String,
        val InstanceRole: String,
        val InstanceType: String,
        val JobFlowId: String,
        val Market: String? = null,
        val Name: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EMR::Step")
    data class Step(
        val ActionOnFailure: String,
        val HadoopJarStep: Step.HadoopJarStepConfigProperty,
        val JobFlowId: String,
        val Name: String
    ) : ResourceProperties {

        data class HadoopJarStepConfigProperty(
            val Args: List<String>? = null,
            val Jar: String,
            val MainClass: String? = null,
            val StepProperties: List<EMR.Step.HadoopJarStepConfigProperty.KeyValueProperty>? = null
        ) {

            data class KeyValueProperty(
                val Key: String? = null,
                val Value: String? = null
            ) 

        }

    }


}