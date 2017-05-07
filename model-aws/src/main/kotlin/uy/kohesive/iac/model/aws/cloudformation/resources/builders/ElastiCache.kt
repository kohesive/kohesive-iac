package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.elasticache.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.ElastiCache

class ElastiCacheSecurityGroupIngressResourcePropertiesBuilder : ResourcePropertiesBuilder<AuthorizeCacheSecurityGroupIngressRequest> {

    override val requestClazz = AuthorizeCacheSecurityGroupIngressRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as AuthorizeCacheSecurityGroupIngressRequest).let {
            ElastiCache.SecurityGroupIngress(
                CacheSecurityGroupName  = it.cacheSecurityGroupName,
                EC2SecurityGroupName    = it.eC2SecurityGroupName,
                EC2SecurityGroupOwnerId = it.eC2SecurityGroupOwnerId
            )
        }
}

class ElastiCacheCacheClusterResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateCacheClusterRequest> {

    override val requestClazz = CreateCacheClusterRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateCacheClusterRequest).let {
            ElastiCache.CacheCluster(
                AZMode                     = request.azMode,
                AutoMinorVersionUpgrade    = request.autoMinorVersionUpgrade?.toString(),
                CacheNodeType              = request.cacheNodeType,
                CacheParameterGroupName    = request.cacheParameterGroupName,
                CacheSecurityGroupNames    = request.cacheSecurityGroupNames,
                CacheSubnetGroupName       = request.cacheSubnetGroupName,
                ClusterName                = request.cacheClusterId,
                Engine                     = request.engine,
                EngineVersion              = request.engineVersion,
                NotificationTopicArn       = request.notificationTopicArn,
                NumCacheNodes              = request.numCacheNodes.toString(),
                Port                       = request.port?.toString(),
                PreferredAvailabilityZone  = request.preferredAvailabilityZone,
                PreferredAvailabilityZones = request.preferredAvailabilityZones,
                PreferredMaintenanceWindow = request.preferredMaintenanceWindow,
                SnapshotArns               = request.snapshotArns,
                SnapshotName               = request.snapshotName,
                SnapshotRetentionLimit     = request.snapshotRetentionLimit?.toString(),
                SnapshotWindow             = request.snapshotWindow,
                VpcSecurityGroupIds        = request.securityGroupIds,
                Tags                       = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

class ElastiCacheReplicationGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateReplicationGroupRequest> {

    override val requestClazz = CreateReplicationGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateReplicationGroupRequest).let {
            ElastiCache.ReplicationGroup(
                AutoMinorVersionUpgrade  = request.autoMinorVersionUpgrade?.toString(),
                AutomaticFailoverEnabled = request.automaticFailoverEnabled?.toString(),
                CacheNodeType            = request.cacheNodeType,
                CacheParameterGroupName  = request.cacheParameterGroupName,
                CacheSecurityGroupNames  = request.cacheSecurityGroupNames,
                CacheSubnetGroupName     = request.cacheSubnetGroupName,
                Engine                   = request.engine,
                EngineVersion            = request.engineVersion,
                NodeGroupConfiguration   = request.nodeGroupConfiguration?.map {
                    ElastiCache.ReplicationGroup.NodeGroupConfigurationProperty(
                        PrimaryAvailabilityZone  = it.primaryAvailabilityZone,
                        ReplicaAvailabilityZones = it.replicaAvailabilityZones,
                        ReplicaCount             = it.replicaCount?.toString(),
                        Slots                    = it.slots
                    )
                },
                NotificationTopicArn        = request.notificationTopicArn,
                NumCacheClusters            = request.numCacheClusters?.toString(),
                NumNodeGroups               = request.numNodeGroups?.toString(),
                Port                        = request.port?.toString(),
                PreferredCacheClusterAZs    = request.preferredCacheClusterAZs,
                PreferredMaintenanceWindow  = request.preferredMaintenanceWindow,
                PrimaryClusterId            = request.primaryClusterId,
                ReplicasPerNodeGroup        = request.replicasPerNodeGroup?.toString(),
                ReplicationGroupDescription = request.replicationGroupDescription,
                ReplicationGroupId          = request.replicationGroupId,
                SecurityGroupIds            = request.securityGroupIds,
                SnapshotArns                = request.snapshotArns,
                SnapshotName                = request.snapshotName,
                SnapshotRetentionLimit      = request.snapshotRetentionLimit?.toString(),
                SnapshotWindow              = request.snapshotWindow,
                SnapshottingClusterId       = relatedObjects.filterIsInstance<ModifyReplicationGroupRequest>()?.firstOrNull()?.snapshottingClusterId,
                Tags                        = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

class ElastiCacheSecurityGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateCacheSecurityGroupRequest> {

    override val requestClazz = CreateCacheSecurityGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateCacheSecurityGroupRequest).let {
            ElastiCache.SecurityGroup(
                Description = request.description
            )
        }

}

class ElastiCacheSubnetGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateCacheSubnetGroupRequest> {

    override val requestClazz = CreateCacheSubnetGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateCacheSubnetGroupRequest).let {
            ElastiCache.SubnetGroup(
                CacheSubnetGroupName = request.cacheSubnetGroupName,
                Description          = request.cacheSubnetGroupDescription,
                SubnetIds            = request.subnetIds
            )
        }

}

class ElastiCacheParameterGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateCacheParameterGroupRequest> {

    override val requestClazz = CreateCacheParameterGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateCacheParameterGroupRequest).let {
            ElastiCache.ParameterGroup(
                CacheParameterGroupFamily = request.cacheParameterGroupFamily,
                Description               = request.description,
                Properties                = relatedObjects.filterIsInstance<ModifyCacheParameterGroupRequest>().flatMap { it.parameterNameValues }.associate {
                    it.parameterName to it.parameterValue
                }
            )
        }

}


