package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes
import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties

@CloudFormationTypes
object ElastiCache {

    @CloudFormationType("AWS::ElastiCache::CacheCluster")
    data class CacheCluster(
        val AutoMinorVersionUpgrade: String? = null,
        val AZMode: String? = null,
        val CacheNodeType: String,
        val CacheParameterGroupName: String? = null,
        val CacheSecurityGroupNames: List<String>? = null,
        val CacheSubnetGroupName: String? = null,
        val ClusterName: String? = null,
        val Engine: String,
        val EngineVersion: String? = null,
        val NotificationTopicArn: String? = null,
        val NumCacheNodes: String,
        val Port: String? = null,
        val PreferredAvailabilityZone: String? = null,
        val PreferredAvailabilityZones: List<String>? = null,
        val PreferredMaintenanceWindow: String? = null,
        val SnapshotArns: List<String>? = null,
        val SnapshotName: String? = null,
        val SnapshotRetentionLimit: String? = null,
        val SnapshotWindow: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val VpcSecurityGroupIds: List<String>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::ElastiCache::ParameterGroup")
    data class ParameterGroup(
        val CacheParameterGroupFamily: String,
        val Description: String,
        val Properties: Map<String, String>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::ElastiCache::ReplicationGroup")
    data class ReplicationGroup(
        val AutomaticFailoverEnabled: String? = null,
        val AutoMinorVersionUpgrade: String? = null,
        val CacheNodeType: String? = null,
        val CacheParameterGroupName: String? = null,
        val CacheSecurityGroupNames: List<String>? = null,
        val CacheSubnetGroupName: String? = null,
        val Engine: String,
        val EngineVersion: String? = null,
        val NodeGroupConfiguration: List<ElastiCache.ReplicationGroup.NodeGroupConfigurationProperty>? = null,
        val NotificationTopicArn: String? = null,
        val NumCacheClusters: String? = null,
        val NumNodeGroups: String? = null,
        val Port: String? = null,
        val PreferredCacheClusterAZs: List<String>? = null,
        val PreferredMaintenanceWindow: String? = null,
        val PrimaryClusterId: String? = null,
        val ReplicasPerNodeGroup: String? = null,
        val ReplicationGroupDescription: String,
        val ReplicationGroupId: String? = null,
        val SecurityGroupIds: List<String>? = null,
        val SnapshotArns: List<String>? = null,
        val SnapshotName: String? = null,
        val SnapshotRetentionLimit: String? = null,
        val SnapshottingClusterId: String? = null,
        val SnapshotWindow: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties {

        data class NodeGroupConfigurationProperty(
            val PrimaryAvailabilityZone: String? = null,
            val ReplicaAvailabilityZones: List<String>? = null,
            val ReplicaCount: String? = null,
            val Slots: String? = null
        ) 

    }

    @CloudFormationType("AWS::ElastiCache::SecurityGroup")
    data class SecurityGroup(
        val Description: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::ElastiCache::SecurityGroupIngress")
    data class SecurityGroupIngress(
        val CacheSecurityGroupName: String,
        val EC2SecurityGroupName: String,
        val EC2SecurityGroupOwnerId: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::ElastiCache::SubnetGroup")
    data class SubnetGroup(
        val CacheSubnetGroupName: String? = null,
        val Description: String,
        val SubnetIds: List<String>
    ) : ResourceProperties 


}