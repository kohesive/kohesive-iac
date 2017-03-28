package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object RDS {

    @CloudFormationType("AWS::RDS::DBCluster")
    data class DBCluster(
        val AvailabilityZones: String? = null,
        val BackupRetentionPeriod: String? = null,
        val DatabaseName: String? = null,
        val DBClusterParameterGroupName: String? = null,
        val DBSubnetGroupName: String? = null,
        val Engine: String,
        val EngineVersion: String? = null,
        val KmsKeyId: String? = null,
        val MasterUsername: String? = null,
        val MasterUserPassword: String? = null,
        val Port: String? = null,
        val PreferredBackupWindow: String? = null,
        val PreferredMaintenanceWindow: String? = null,
        val SnapshotIdentifier: String? = null,
        val StorageEncrypted: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val VpcSecurityGroupIds: List<String>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::RDS::DBClusterParameterGroup")
    data class DBClusterParameterGroup(
        val Description: String,
        val Family: String,
        val Parameters: Any,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::RDS::DBInstance")
    data class DBInstance(
        val AllocatedStorage: String? = null,
        val AllowMajorVersionUpgrade: String? = null,
        val AutoMinorVersionUpgrade: String? = null,
        val AvailabilityZone: String? = null,
        val BackupRetentionPeriod: String? = null,
        val CharacterSetName: String? = null,
        val CopyTagsToSnapshot: String? = null,
        val DBClusterIdentifier: String? = null,
        val DBInstanceClass: String,
        val DBInstanceIdentifier: String? = null,
        val DBName: String? = null,
        val DBParameterGroupName: String? = null,
        val DBSecurityGroups: List<String>? = null,
        val DBSnapshotIdentifier: String? = null,
        val DBSubnetGroupName: String? = null,
        val Domain: String? = null,
        val DomainIAMRoleName: String? = null,
        val Engine: String? = null,
        val EngineVersion: String? = null,
        val Iops: String? = null,
        val KmsKeyId: String? = null,
        val LicenseModel: String? = null,
        val MasterUsername: String? = null,
        val MasterUserPassword: String? = null,
        val MonitoringInterval: String? = null,
        val MonitoringRoleArn: String? = null,
        val MultiAZ: String? = null,
        val OptionGroupName: String? = null,
        val Port: String? = null,
        val PreferredBackupWindow: String? = null,
        val PreferredMaintenanceWindow: String? = null,
        val PubliclyAccessible: String? = null,
        val SourceDBInstanceIdentifier: String? = null,
        val StorageEncrypted: String? = null,
        val StorageType: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val Timezone: String? = null,
        val VPCSecurityGroups: List<String>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::RDS::DBParameterGroup")
    data class DBParameterGroup(
        val Description: String,
        val Family: String,
        val Parameters: Any? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::RDS::DBSecurityGroup")
    data class DBSecurityGroup(
        val EC2VpcId: String? = null,
        val DBSecurityGroupIngress: List<RDS.DBSecurityGroup.RuleProperty>,
        val GroupDescription: String,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties {

        data class RuleProperty(
            val CIDRIP: String? = null,
            val EC2SecurityGroupId: String? = null,
            val EC2SecurityGroupName: String? = null,
            val EC2SecurityGroupOwnerId: String? = null
        ) 

    }

    @CloudFormationType("AWS::RDS::DBSecurityGroupIngress")
    data class DBSecurityGroupIngress(
        val CIDRIP: String,
        val DBSecurityGroupName: String,
        val EC2SecurityGroupId: String? = null,
        val EC2SecurityGroupName: String? = null,
        val EC2SecurityGroupOwnerId: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::RDS::DBSubnetGroup")
    data class DBSubnetGroup(
        val DBSubnetGroupDescription: String,
        val SubnetIds: List<String>,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::RDS::EventSubscription")
    data class EventSubscription(
        val Enabled: String? = null,
        val EventCategories: List<String>? = null,
        val SnsTopicArn: String,
        val SourceIds: List<String>? = null,
        val SourceType: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::RDS::OptionGroup")
    data class OptionGroup(
        val EngineName: String,
        val MajorEngineVersion: String,
        val OptionGroupDescription: String,
        val OptionConfigurations: List<RDS.OptionGroup.OptionConfigurationProperty>,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties {

        data class OptionConfigurationProperty(
            val DBSecurityGroupMemberships: List<String>? = null,
            val OptionName: String,
            val OptionSettings: List<RDS.OptionGroup.OptionConfigurationProperty.OptionSettingProperty>? = null,
            val Port: String? = null,
            val VpcSecurityGroupMemberships: List<String>? = null
        ) {

            data class OptionSettingProperty(
                val Name: String? = null,
                val Value: String? = null
            ) 

        }

    }


}