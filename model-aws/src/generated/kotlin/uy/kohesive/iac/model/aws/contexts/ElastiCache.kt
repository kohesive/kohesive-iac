package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.elasticache.AmazonElastiCache
import com.amazonaws.services.elasticache.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ElastiCacheIdentifiable : KohesiveIdentifiable {

}

interface ElastiCacheEnabled : ElastiCacheIdentifiable {
    val elastiCacheClient: AmazonElastiCache
    val elastiCacheContext: ElastiCacheContext
    fun <T> withElastiCacheContext(init: ElastiCacheContext.(AmazonElastiCache) -> T): T = elastiCacheContext.init(elastiCacheClient)
}

open class BaseElastiCacheContext(protected val context: IacContext) : ElastiCacheEnabled by context {

    fun createCacheParameterGroup(cacheParameterGroupName: String, init: CreateCacheParameterGroupRequest.() -> Unit): CacheParameterGroup {
        return elastiCacheClient.createCacheParameterGroup(CreateCacheParameterGroupRequest().apply {
            withCacheParameterGroupName(cacheParameterGroupName)
            init()
        })
    }

    fun createCacheSecurityGroup(cacheSecurityGroupName: String, init: CreateCacheSecurityGroupRequest.() -> Unit): CacheSecurityGroup {
        return elastiCacheClient.createCacheSecurityGroup(CreateCacheSecurityGroupRequest().apply {
            withCacheSecurityGroupName(cacheSecurityGroupName)
            init()
        })
    }

    fun createCacheSubnetGroup(cacheSubnetGroupName: String, init: CreateCacheSubnetGroupRequest.() -> Unit): CacheSubnetGroup {
        return elastiCacheClient.createCacheSubnetGroup(CreateCacheSubnetGroupRequest().apply {
            withCacheSubnetGroupName(cacheSubnetGroupName)
            init()
        })
    }

    fun createSnapshot(snapshotName: String, init: CreateSnapshotRequest.() -> Unit): Snapshot {
        return elastiCacheClient.createSnapshot(CreateSnapshotRequest().apply {
            withSnapshotName(snapshotName)
            init()
        })
    }


}

@DslScope
class ElastiCacheContext(context: IacContext) : BaseElastiCacheContext(context) {

}
