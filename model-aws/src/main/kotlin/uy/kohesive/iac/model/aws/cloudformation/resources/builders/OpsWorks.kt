package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.opsworks.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.OpsWorks

class OpsWorksAppResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateAppRequest> {

    override val requestClazz = CreateAppRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAppRequest).let {
            OpsWorks.App(
                AppSource   = request.appSource?.toCF(),
                Attributes  = request.attributes,
                DataSources = request.dataSources?.map {
                    OpsWorks.App.DataSourceProperty(
                        Arn          = it.arn,
                        Type         = it.type,
                        DatabaseName = it.databaseName
                    )
                },
                Description = request.description,
                Domains     = request.domains,
                EnableSsl   = request.enableSsl?.toString(),
                Environment = request.environment?.map {
                    OpsWorks.App.EnvironmentProperty(
                        Key    = it.key,
                        Value  = it.value,
                        Secure = it.secure?.toString()
                    )
                },
                Name             = request.name,
                Shortname        = request.shortname,
                SslConfiguration = request.sslConfiguration?.let {
                    OpsWorks.App.SslConfigurationProperty(
                        Certificate = it.certificate,
                        Chain       = it.chain,
                        PrivateKey  = it.privateKey
                    )
                },
                StackId = request.stackId,
                Type    = request.type
            )
        }

}

class OpsWorksInstanceResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateInstanceRequest> {

    override val requestClazz = CreateInstanceRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateInstanceRequest).let {
            OpsWorks.Instance(
                AgentVersion        = request.agentVersion,
                AmiId               = request.amiId,
                Architecture        = request.architecture,
                AutoScalingType     = request.autoScalingType,
                AvailabilityZone    = request.availabilityZone,
                BlockDeviceMappings = request.blockDeviceMappings?.map {
                    OpsWorks.Instance.BlockDeviceMappingProperty(
                        DeviceName  = it.deviceName,
                        VirtualName = it.virtualName,
                        NoDevice    = it.noDevice,
                        Ebs         = it.ebs?.let {
                            OpsWorks.Instance.BlockDeviceMappingProperty.EbsBlockDeviceProperty(
                                DeleteOnTermination = it.deleteOnTermination?.toString(),
                                VolumeType          = it.volumeType,
                                VolumeSize          = it.volumeSize?.toString(),
                                SnapshotId          = it.snapshotId,
                                Iops                = it.iops?.toString()
                            )
                        }
                    )
                },
                EbsOptimized         = request.ebsOptimized?.toString(),
                Hostname             = request.hostname,
                InstallUpdatesOnBoot = request.installUpdatesOnBoot?.toString(),
                InstanceType         = request.instanceType,
                LayerIds             = request.layerIds,
                Os                   = request.os,
                RootDeviceType       = request.rootDeviceType,
                SshKeyName           = request.sshKeyName,
                StackId              = request.stackId,
                SubnetId             = request.subnetId,
                Tenancy              = request.tenancy,
                VirtualizationType   = request.virtualizationType,
                ElasticIps           = relatedObjects.filterIsInstance<AssociateElasticIpRequest>().map {
                    it.elasticIp
                },
                TimeBasedAutoScaling = relatedObjects.filterIsInstance<SetTimeBasedAutoScalingRequest>().lastOrNull()?.autoScalingSchedule?.let {
                    OpsWorks.Instance.TimeBasedAutoScalingProperty(
                        Monday    = it.monday,
                        Tuesday   = it.tuesday,
                        Wednesday = it.wednesday,
                        Thursday  = it.thursday,
                        Friday    = it.friday,
                        Saturday  = it.saturday,
                        Sunday    = it.sunday
                    )
                },
                Volumes = relatedObjects.filterIsInstance<AssignVolumeRequest>().map {
                    it.volumeId
                }
            )
        }

}

class OpsWorksLayerResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateLayerRequest> {

    override val requestClazz = CreateLayerRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateLayerRequest).let {
            OpsWorks.Layer(
                Attributes               = request.attributes,
                AutoAssignElasticIps     = request.autoAssignElasticIps.toString(),
                AutoAssignPublicIps      = request.autoAssignPublicIps.toString(),
                CustomInstanceProfileArn = request.customInstanceProfileArn,
                CustomJson               = request.customJson,
                CustomRecipes            = request.customRecipes?.let {
                    OpsWorks.Layer.RecipeProperty(
                        Configure = it.configure,
                        Deploy    = it.deploy,
                        Setup     = it.setup,
                        Shutdown  = it.shutdown,
                        Undeploy  = it.undeploy
                    )
                },
                CustomSecurityGroupIds      = request.customSecurityGroupIds,
                EnableAutoHealing           = request.enableAutoHealing.toString(),
                InstallUpdatesOnBoot        = request.installUpdatesOnBoot?.toString(),
                LifecycleEventConfiguration = request.lifecycleEventConfiguration?.let {
                    OpsWorks.Layer.LifeCycleConfigurationProperty(
                        ShutdownEventConfiguration = it.shutdown?.let {
                            OpsWorks.Layer.LifeCycleConfigurationProperty.ShutdownEventConfigurationProperty(
                                DelayUntilElbConnectionsDrained = it.delayUntilElbConnectionsDrained?.toString(),
                                ExecutionTimeout                = it.executionTimeout?.toString()
                            )
                        }
                    )
                },
                Name      = request.name,
                Packages  = request.packages,
                Shortname = request.shortname,
                StackId   = request.stackId,
                Type      = request.type,
                VolumeConfigurations = request.volumeConfigurations?.map {
                    OpsWorks.Layer.VolumeConfigurationProperty(
                        Iops          = it.iops?.toString(),
                        VolumeType    = it.volumeType,
                        Size          = it.size.toString(),
                        MountPoint    = it.mountPoint,
                        NumberOfDisks = it.numberOfDisks.toString(),
                        RaidLevel     = it.raidLevel?.toString()
                    )
                },
                LoadBasedAutoScaling = relatedObjects.filterIsInstance<SetLoadBasedAutoScalingRequest>().lastOrNull()?.let {
                    OpsWorks.Layer.LoadBasedAutoScalingProperty(
                        DownScaling = it.downScaling?.toCF(),
                        UpScaling   = it.upScaling?.toCF(),
                        Enable      = it.enable?.toString()
                    )
                }
            )
        }

    fun AutoScalingThresholds.toCF() = OpsWorks.Layer.LoadBasedAutoScalingProperty.AutoScalingThresholdProperty(
       CpuThreshold       = cpuThreshold?.toString(),
       IgnoreMetricsTime  = ignoreMetricsTime?.toString(),
       InstanceCount      = instanceCount?.toString(),
       LoadThreshold      = loadThreshold?.toString(),
       MemoryThreshold    = memoryThreshold?.toString(),
       ThresholdsWaitTime = thresholdsWaitTime?.toString()
   )

}

// TODO: Stack can be cloned, thus created from CloneStackRequest. Also, mind the SourceStackId

class OpsWorksStackResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateStackRequest> {

    override val requestClazz = CreateStackRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateStackRequest).let {
            OpsWorks.Stack(
                AgentVersion      = request.agentVersion,
                Attributes        = request.attributes,
                ChefConfiguration = request.chefConfiguration?.let {
                    OpsWorks.Stack.ChefConfigurationProperty(
                        BerkshelfVersion = it.berkshelfVersion,
                        ManageBerkshelf  = it.manageBerkshelf?.toString()
                    )
                },
                ConfigurationManager = request.configurationManager?.let {
                    OpsWorks.Stack.StackConfigurationManagerProperty(
                        Name    = it.name,
                        Version = it.version
                    )
                },
//                CloneAppIds      = relatedObjects.filterIsInstance<CloneStackRequest>().lastOrNull()?.cloneAppIds,
//                ClonePermissions = relatedObjects.filterIsInstance<CloneStackRequest>().lastOrNull()?.clonePermissions?.toString(),
                EcsClusterArn    = relatedObjects.filterIsInstance<RegisterEcsClusterRequest>().lastOrNull()?.ecsClusterArn,
                ElasticIps       = relatedObjects.filterIsInstance<RegisterElasticIpRequest>().map {
                    OpsWorks.Stack.ElasticIpProperty(
                        Ip = it.elasticIp
                    )
                },
                CustomCookbooksSource     = request.customCookbooksSource?.toCF(),
                CustomJson                = request.customJson,
                DefaultAvailabilityZone   = request.defaultAvailabilityZone,
                DefaultInstanceProfileArn = request.defaultInstanceProfileArn,
                DefaultOs                 = request.defaultOs,
                DefaultRootDeviceType     = request.defaultRootDeviceType,
                DefaultSshKeyName         = request.defaultSshKeyName,
                DefaultSubnetId           = request.defaultSubnetId,
                HostnameTheme             = request.hostnameTheme,
                Name                      = request.name,
                RdsDbInstances            = relatedObjects.filterIsInstance<RegisterRdsDbInstanceRequest>().map {
                    OpsWorks.Stack.RdsDbInstanceProperty(
                        DbPassword       = it.dbPassword,
                        DbUser           = it.dbUser,
                        RdsDbInstanceArn = it.rdsDbInstanceArn
                    )
                },
                ServiceRoleArn            = request.serviceRoleArn,
                UseCustomCookbooks        = request.useCustomCookbooks?.toString(),
                UseOpsworksSecurityGroups = request.useOpsworksSecurityGroups?.toString(),
                VpcId                     = request.vpcId
            )
        }

}

class OpsWorksUserProfileResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateUserProfileRequest> {

    override val requestClazz = CreateUserProfileRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateUserProfileRequest).let {
            OpsWorks.UserProfile(
                AllowSelfManagement = request.allowSelfManagement?.toString(),
                IamUserArn          = request.iamUserArn,
                SshPublicKey        = request.sshPublicKey
            )
        }

}

class OpsWorksVolumeResourcePropertiesBuilder : ResourcePropertiesBuilder<RegisterVolumeRequest> {

    override val requestClazz = RegisterVolumeRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as RegisterVolumeRequest).let {
            val updateVolumeRequest = relatedObjects.filterIsInstance<UpdateVolumeRequest>().lastOrNull()

            OpsWorks.Volume(
                Ec2VolumeId = request.ec2VolumeId,
                StackId     = request.stackId,
                MountPoint  = updateVolumeRequest?.mountPoint,
                Name        = updateVolumeRequest?.name
            )
        }

}

fun Source.toCF() = OpsWorks.Stack.SourceProperty(
    Password = password,
    Username = username,
    Type     = type,
    Revision = revision,
    SshKey   = sshKey,
    Url      = url
)
