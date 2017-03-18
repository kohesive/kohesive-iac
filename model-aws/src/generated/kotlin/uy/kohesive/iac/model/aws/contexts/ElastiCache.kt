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

}

@DslScope
class ElastiCacheContext(context: IacContext) : BaseElastiCacheContext(context) {

}