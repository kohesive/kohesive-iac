package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object ECS {

    @CloudFormationType("AWS::ECS::Cluster")
    data class Cluster(
        val ClusterName: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::ECS::Service")
    data class Service(
        val Cluster: String? = null,
        val DeploymentConfiguration: Service.DeploymentConfigurationProperty? = null,
        val DesiredCount: String,
        val LoadBalancers: List<ECS.Service.LoadBalancerProperty>? = null,
        val Role: String? = null,
        val TaskDefinition: String
    ) : ResourceProperties {

        data class DeploymentConfigurationProperty(
            val MaximumPercent: String? = null,
            val MinimumHealthyPercent: String? = null
        ) 


        data class LoadBalancerProperty(
            val ContainerName: String,
            val ContainerPort: String,
            val LoadBalancerName: String? = null,
            val TargetGroupArn: String? = null
        ) 

    }

    @CloudFormationType("AWS::ECS::TaskDefinition")
    data class TaskDefinition(
        val ContainerDefinitions: List<ECS.TaskDefinition.ContainerDefinitionProperty>,
        val Family: String? = null,
        val NetworkMode: String? = null,
        val TaskRoleArn: String? = null,
        val Volumes: List<ECS.TaskDefinition.VolumeProperty>
    ) : ResourceProperties {

        data class ContainerDefinitionProperty(
            val Command: List<String>? = null,
            val Cpu: String? = null,
            val DisableNetworking: String? = null,
            val DnsSearchDomains: List<String>? = null,
            val DnsServers: List<String>? = null,
            val DockerLabels: Map<String, String>? = null,
            val DockerSecurityOptions: List<String>? = null,
            val EntryPoint: List<String>? = null,
            val Environment: List<ECS.TaskDefinition.ContainerDefinitionProperty.EnvironmentProperty>? = null,
            val Essential: String? = null,
            val ExtraHosts: List<ECS.TaskDefinition.ContainerDefinitionProperty.HostEntryProperty>? = null,
            val Hostname: String? = null,
            val Image: String,
            val Links: List<String>? = null,
            val LogConfiguration: TaskDefinition.ContainerDefinitionProperty.LogConfigurationProperty? = null,
            val Memory: String? = null,
            val MemoryReservation: String? = null,
            val MountPoints: List<ECS.TaskDefinition.ContainerDefinitionProperty.MountPointProperty>? = null,
            val Name: String,
            val PortMappings: List<ECS.TaskDefinition.ContainerDefinitionProperty.PortMappingProperty>? = null,
            val Privileged: String? = null,
            val ReadonlyRootFilesystem: String? = null,
            val Ulimits: List<ECS.TaskDefinition.ContainerDefinitionProperty.UlimitProperty>? = null,
            val User: String? = null,
            val VolumesFrom: List<ECS.TaskDefinition.ContainerDefinitionProperty.VolumesFromProperty>? = null,
            val WorkingDirectory: String? = null
        ) {

            data class EnvironmentProperty(
                val Name: String,
                val Value: String
            ) 


            data class HostEntryProperty(
                val Hostname: String,
                val IpAddress: String
            ) 


            data class LogConfigurationProperty(
                val LogDriver: String,
                val Options: Map<String, String>? = null
            ) 


            data class MountPointProperty(
                val ContainerPath: String,
                val SourceVolume: String,
                val ReadOnly: String? = null
            ) 


            data class PortMappingProperty(
                val ContainerPort: String,
                val HostPort: String? = null,
                val Protocol: String? = null
            ) 


            data class UlimitProperty(
                val HardLimit: String,
                val Name: String? = null,
                val SoftLimit: String
            ) 


            data class VolumesFromProperty(
                val SourceContainer: String,
                val ReadOnly: String? = null
            ) 

        }


        data class VolumeProperty(
            val Name: String,
            val Host: TaskDefinition.VolumeProperty.HostProperty? = null
        ) {

            data class HostProperty(
                val SourcePath: String? = null
            ) 

        }

    }


}