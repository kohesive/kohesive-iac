package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticache.AbstractAmazonElastiCache
import com.amazonaws.services.elasticache.AmazonElastiCache
import com.amazonaws.services.elasticache.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonElastiCache(val context: IacContext) : AbstractAmazonElastiCache(), AmazonElastiCache {

    override fun addTagsToResource(request: AddTagsToResourceRequest): AddTagsToResourceResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddTagsToResourceRequest, AddTagsToResourceResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createCacheCluster(request: CreateCacheClusterRequest): CacheCluster {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateCacheClusterRequest, CacheCluster>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateCacheClusterRequest::getCacheClusterId to CacheCluster::getCacheClusterId,
                    CreateCacheClusterRequest::getCacheNodeType to CacheCluster::getCacheNodeType,
                    CreateCacheClusterRequest::getEngine to CacheCluster::getEngine,
                    CreateCacheClusterRequest::getEngineVersion to CacheCluster::getEngineVersion,
                    CreateCacheClusterRequest::getNumCacheNodes to CacheCluster::getNumCacheNodes,
                    CreateCacheClusterRequest::getPreferredAvailabilityZone to CacheCluster::getPreferredAvailabilityZone,
                    CreateCacheClusterRequest::getPreferredMaintenanceWindow to CacheCluster::getPreferredMaintenanceWindow,
                    CreateCacheClusterRequest::getCacheSubnetGroupName to CacheCluster::getCacheSubnetGroupName,
                    CreateCacheClusterRequest::getAutoMinorVersionUpgrade to CacheCluster::getAutoMinorVersionUpgrade,
                    CreateCacheClusterRequest::getReplicationGroupId to CacheCluster::getReplicationGroupId,
                    CreateCacheClusterRequest::getSnapshotRetentionLimit to CacheCluster::getSnapshotRetentionLimit,
                    CreateCacheClusterRequest::getSnapshotWindow to CacheCluster::getSnapshotWindow
                )
            )
        }
    }

    override fun createCacheParameterGroup(request: CreateCacheParameterGroupRequest): CacheParameterGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateCacheParameterGroupRequest, CacheParameterGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateCacheParameterGroupRequest::getCacheParameterGroupName to CacheParameterGroup::getCacheParameterGroupName,
                    CreateCacheParameterGroupRequest::getCacheParameterGroupFamily to CacheParameterGroup::getCacheParameterGroupFamily,
                    CreateCacheParameterGroupRequest::getDescription to CacheParameterGroup::getDescription
                )
            )
        }
    }

    override fun createCacheSecurityGroup(request: CreateCacheSecurityGroupRequest): CacheSecurityGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateCacheSecurityGroupRequest, CacheSecurityGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateCacheSecurityGroupRequest::getCacheSecurityGroupName to CacheSecurityGroup::getCacheSecurityGroupName,
                    CreateCacheSecurityGroupRequest::getDescription to CacheSecurityGroup::getDescription
                )
            )
        }
    }

    override fun createCacheSubnetGroup(request: CreateCacheSubnetGroupRequest): CacheSubnetGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateCacheSubnetGroupRequest, CacheSubnetGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateCacheSubnetGroupRequest::getCacheSubnetGroupName to CacheSubnetGroup::getCacheSubnetGroupName,
                    CreateCacheSubnetGroupRequest::getCacheSubnetGroupDescription to CacheSubnetGroup::getCacheSubnetGroupDescription
                )
            )
        }
    }

    override fun createReplicationGroup(request: CreateReplicationGroupRequest): ReplicationGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateReplicationGroupRequest, ReplicationGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateReplicationGroupRequest::getReplicationGroupId to ReplicationGroup::getReplicationGroupId,
                    CreateReplicationGroupRequest::getSnapshotRetentionLimit to ReplicationGroup::getSnapshotRetentionLimit,
                    CreateReplicationGroupRequest::getSnapshotWindow to ReplicationGroup::getSnapshotWindow
                )
            )
        }
    }

    override fun createSnapshot(request: CreateSnapshotRequest): Snapshot {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateSnapshotRequest, Snapshot>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateSnapshotRequest::getSnapshotName to Snapshot::getSnapshotName,
                    CreateSnapshotRequest::getReplicationGroupId to Snapshot::getReplicationGroupId,
                    CreateSnapshotRequest::getCacheClusterId to Snapshot::getCacheClusterId
                )
            )
        }
    }


}

class DeferredAmazonElastiCache(context: IacContext) : BaseDeferredAmazonElastiCache(context)
