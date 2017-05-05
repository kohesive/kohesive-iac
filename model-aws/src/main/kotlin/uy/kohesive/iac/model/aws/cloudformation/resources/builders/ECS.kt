package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.ecs.model.CreateClusterRequest
import com.amazonaws.services.ecs.model.CreateServiceRequest
import com.amazonaws.services.ecs.model.RegisterTaskDefinitionRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.ECS

class ECSTaskDefinitionResourcePropertiesBuilder : ResourcePropertiesBuilder<RegisterTaskDefinitionRequest> {

    override val requestClazz = RegisterTaskDefinitionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as RegisterTaskDefinitionRequest).let {
            ECS.TaskDefinition(
                ContainerDefinitions = request.containerDefinitions.map {
                    ECS.TaskDefinition.ContainerDefinitionProperty(
                        Command               = it.command,
                        Name                  = it.name,
                        Image                 = it.image,
                        Cpu                   = it.cpu?.toString(),
                        DisableNetworking     = it.disableNetworking?.toString(),
                        DnsSearchDomains      = it.dnsSearchDomains,
                        DnsServers            = it.dnsServers,
                        DockerLabels          = it.dockerLabels,
                        DockerSecurityOptions = it.dockerSecurityOptions,
                        EntryPoint            = it.entryPoint,
                        Environment           = it.environment?.map {
                            ECS.TaskDefinition.ContainerDefinitionProperty.EnvironmentProperty(
                                Name  = it.name,
                                Value = it.value
                            )
                        },
                        Essential  = it.essential?.toString(),
                        ExtraHosts = it.extraHosts?.map {
                            ECS.TaskDefinition.ContainerDefinitionProperty.HostEntryProperty(
                                Hostname  = it.hostname,
                                IpAddress = it.ipAddress
                            )
                        },
                        Hostname = it.hostname,
                        Links    = it.links,
                        LogConfiguration = it.logConfiguration?.let {
                            ECS.TaskDefinition.ContainerDefinitionProperty.LogConfigurationProperty(
                                LogDriver = it.logDriver,
                                Options   = it.options
                            )
                        },
                        Memory            = it.memory?.toString(),
                        MemoryReservation = it.memoryReservation?.toString(),
                        MountPoints       = it.mountPoints?.map {
                            ECS.TaskDefinition.ContainerDefinitionProperty.MountPointProperty(
                                ContainerPath = it.containerPath,
                                ReadOnly      = it.readOnly?.toString(),
                                SourceVolume  = it.sourceVolume
                            )
                        },
                        PortMappings = it.portMappings?.map {
                            ECS.TaskDefinition.ContainerDefinitionProperty.PortMappingProperty(
                                ContainerPort = it.containerPort.toString(),
                                HostPort      = it.hostPort?.toString(),
                                Protocol      = it.protocol
                            )
                        },
                        Privileged             = it.privileged?.toString(),
                        ReadonlyRootFilesystem = it.readonlyRootFilesystem?.toString(),
                        Ulimits                = it.ulimits?.map {
                            ECS.TaskDefinition.ContainerDefinitionProperty.UlimitProperty(
                                Name      = it.name,
                                HardLimit = it.hardLimit.toString(),
                                SoftLimit = it.softLimit.toString()
                            )
                        },
                        User        = it.user,
                        VolumesFrom = it.volumesFrom?.map {
                            ECS.TaskDefinition.ContainerDefinitionProperty.VolumesFromProperty(
                                SourceContainer = it.sourceContainer,
                                ReadOnly        = it.readOnly?.toString()
                            )
                        },
                        WorkingDirectory = it.workingDirectory
                    )
                },
                Family      = request.family,
                NetworkMode = request.networkMode,
                TaskRoleArn = request.taskRoleArn,
                Volumes     = request.volumes.map {
                    ECS.TaskDefinition.VolumeProperty(
                        Name = it.name,
                        Host = it.host?.let {
                            ECS.TaskDefinition.VolumeProperty.HostProperty(
                                SourcePath = it.sourcePath
                            )
                        }
                    )
                }
            )
        }

}

class ECSClusterResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateClusterRequest> {

    override val requestClazz = CreateClusterRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateClusterRequest).let {
            ECS.Cluster(
                ClusterName = request.clusterName
            )
        }

}

class ECSServiceResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateServiceRequest> {

    override val requestClazz = CreateServiceRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateServiceRequest).let {
            ECS.Service(
                Cluster                 = request.cluster,
                DeploymentConfiguration = request.deploymentConfiguration?.let {
                    ECS.Service.DeploymentConfigurationProperty(
                        MaximumPercent        = it.maximumPercent?.toString(),
                        MinimumHealthyPercent = it.minimumHealthyPercent?.toString()
                    )
                },
                DesiredCount  = request.desiredCount.toString(),
                LoadBalancers = request.loadBalancers?.map {
                    ECS.Service.LoadBalancerProperty(
                        ContainerName    = it.containerName,
                        ContainerPort    = it.containerPort.toString(),
                        LoadBalancerName = it.loadBalancerName,
                        TargetGroupArn   = it.targetGroupArn
                    )
                },
                Role           = request.role,
                TaskDefinition = request.taskDefinition
            )
        }

}

