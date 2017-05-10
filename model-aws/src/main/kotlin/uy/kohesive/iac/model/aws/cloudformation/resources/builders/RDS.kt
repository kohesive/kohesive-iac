package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.rds.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.RDS

// TODO: Cluster can also be created from a snapshot. Mind the 'SnapshotIdentifier'
class RDSDBClusterResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDBClusterRequest> {

    override val requestClazz = CreateDBClusterRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDBClusterRequest).let {
            RDS.DBCluster(
                AvailabilityZones           = request.availabilityZones,
                BackupRetentionPeriod       = request.backupRetentionPeriod?.toString(),
                DBClusterParameterGroupName = request.dbClusterParameterGroupName,
                DBSubnetGroupName           = request.dbSubnetGroupName,
                DatabaseName                = request.databaseName,
                Engine                      = request.engine,
                EngineVersion               = request.engineVersion,
                KmsKeyId                    = request.kmsKeyId,
                MasterUserPassword          = request.masterUserPassword,
                MasterUsername              = request.masterUsername,
                Port                        = request.port?.toString(),
                PreferredBackupWindow       = request.preferredBackupWindow,
                PreferredMaintenanceWindow  = request.preferredMaintenanceWindow,
                StorageEncrypted            = request.storageEncrypted?.toString(),
                Tags                        = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                },
                VpcSecurityGroupIds = request.vpcSecurityGroupIds
            )
        }

}

class RDSDBClusterParameterGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDBClusterParameterGroupRequest> {

    override val requestClazz = CreateDBClusterParameterGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDBClusterParameterGroupRequest).let {
            RDS.DBClusterParameterGroup(
                Description = request.description,
                Family      = request.dbParameterGroupFamily,
                Parameters  = relatedObjects.filterIsInstance<ModifyDBClusterParameterGroupRequest>().lastOrNull()?.parameters?.map {
                    it.parameterName to it.parameterValue
                }?.toMap(),
                Tags = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

// TODO: Instance can also be created from a snapshot. Mind the 'DBSnapshotIdentifier'
class RDSDBInstanceResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDBInstanceRequest> {

    override val requestClazz = CreateDBInstanceRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDBInstanceRequest).let {
            RDS.DBInstance(
                AllocatedStorage           = request.allocatedStorage?.toString(),
                AllowMajorVersionUpgrade   = relatedObjects.filterIsInstance<ModifyDBInstanceRequest>().lastOrNull()?.allowMajorVersionUpgrade?.toString(),
                AutoMinorVersionUpgrade    = request.autoMinorVersionUpgrade?.toString(),
                AvailabilityZone           = request.availabilityZone,
                BackupRetentionPeriod      = request.backupRetentionPeriod?.toString(),
                CharacterSetName           = request.characterSetName,
                CopyTagsToSnapshot         = request.copyTagsToSnapshot?.toString(),
                DBClusterIdentifier        = request.dbClusterIdentifier,
                DBInstanceClass            = request.dbInstanceClass,
                DBInstanceIdentifier       = request.dbInstanceIdentifier,
                DBName                     = request.dbName,
                DBParameterGroupName       = request.dbParameterGroupName,
                DBSecurityGroups           = request.dbSecurityGroups,
                DBSubnetGroupName          = request.dbSubnetGroupName,
                Domain                     = request.domain,
                DomainIAMRoleName          = request.domainIAMRoleName,
                Engine                     = request.engine,
                EngineVersion              = request.engineVersion,
                Iops                       = request.iops?.toString(),
                KmsKeyId                   = request.kmsKeyId,
                LicenseModel               = request.licenseModel,
                MasterUserPassword         = request.masterUserPassword,
                MasterUsername             = request.masterUsername,
                MonitoringInterval         = request.monitoringInterval?.toString(),
                MonitoringRoleArn          = request.monitoringRoleArn,
                MultiAZ                    = request.multiAZ?.toString(),
                OptionGroupName            = request.optionGroupName,
                Port                       = request.port?.toString(),
                PreferredBackupWindow      = request.preferredBackupWindow,
                PreferredMaintenanceWindow = request.preferredMaintenanceWindow,
                PubliclyAccessible         = request.publiclyAccessible?.toString(),
                StorageEncrypted           = request.storageEncrypted?.toString(),
                StorageType                = request.storageType,
                Tags                       = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                },
                Timezone          = request.timezone,
                VPCSecurityGroups = request.vpcSecurityGroupIds
            )
        }

}

class RDSDBParameterGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDBParameterGroupRequest> {

    override val requestClazz = CreateDBParameterGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDBParameterGroupRequest).let {
            RDS.DBParameterGroup(
                Description = request.description,
                Family      = request.dbParameterGroupFamily,
                Parameters  = relatedObjects.filterIsInstance<ModifyDBParameterGroupRequest>().lastOrNull()?.parameters?.map {
                    it.parameterName to it.parameterValue
                }?.toMap(),
                Tags = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

class RDSDBSecurityGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDBSecurityGroupRequest> {

    override val requestClazz = CreateDBSecurityGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDBSecurityGroupRequest).let {
            RDS.DBSecurityGroup(
                GroupDescription       = request.dbSecurityGroupDescription,
                DBSecurityGroupIngress = relatedObjects.filterIsInstance<AuthorizeDBSecurityGroupIngressRequest>().map {
                    RDS.DBSecurityGroup.RuleProperty(
                        CIDRIP                  = it.cidrip,
                        EC2SecurityGroupOwnerId = it.eC2SecurityGroupOwnerId,
                        EC2SecurityGroupName    = it.eC2SecurityGroupName,
                        EC2SecurityGroupId      = it.eC2SecurityGroupId
                    )
                },
                Tags = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

class RDSDBSubnetGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDBSubnetGroupRequest> {

    override val requestClazz = CreateDBSubnetGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDBSubnetGroupRequest).let {
            RDS.DBSubnetGroup(
                DBSubnetGroupDescription = request.dbSubnetGroupDescription,
                SubnetIds                = request.subnetIds,
                Tags                     = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

class RDSEventSubscriptionResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateEventSubscriptionRequest> {

    override val requestClazz = CreateEventSubscriptionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateEventSubscriptionRequest).let {
            RDS.EventSubscription(
                Enabled         = request.enabled?.toString(),
                EventCategories = request.eventCategories,
                SnsTopicArn     = request.snsTopicArn,
                SourceIds       = request.sourceIds,
                SourceType      = request.sourceType
            )
        }

}

class RDSOptionGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateOptionGroupRequest> {

    override val requestClazz = CreateOptionGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateOptionGroupRequest).let {
            RDS.OptionGroup(
                EngineName             = request.engineName,
                MajorEngineVersion     = request.majorEngineVersion,
                OptionGroupDescription = request.optionGroupDescription,
                OptionConfigurations   = relatedObjects.filterIsInstance<ModifyOptionGroupRequest>().lastOrNull()?.optionsToInclude?.map {
                    RDS.OptionGroup.OptionConfigurationProperty(
                        DBSecurityGroupMemberships = it.dbSecurityGroupMemberships,
                        OptionName                 = it.optionName,
                        OptionSettings             = it.optionSettings?.map {
                            RDS.OptionGroup.OptionConfigurationProperty.OptionSettingProperty(
                                Name  = it.name,
                                Value = it.value
                            )
                        },
                        Port = it.port?.toString(),
                        VpcSecurityGroupMemberships = it.vpcSecurityGroupMemberships
                    )
                },
                Tags                   = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

