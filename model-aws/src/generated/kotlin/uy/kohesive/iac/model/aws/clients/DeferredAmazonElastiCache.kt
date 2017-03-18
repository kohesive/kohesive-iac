package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticache.AbstractAmazonElastiCache
import com.amazonaws.services.elasticache.AmazonElastiCache
import com.amazonaws.services.elasticache.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonElastiCache(val context: IacContext) : AbstractAmazonElastiCache(), AmazonElastiCache {

}

class DeferredAmazonElastiCache(context: IacContext) : BaseDeferredAmazonElastiCache(context)