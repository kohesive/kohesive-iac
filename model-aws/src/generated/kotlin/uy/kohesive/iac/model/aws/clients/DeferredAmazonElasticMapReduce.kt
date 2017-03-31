package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticmapreduce.AbstractAmazonElasticMapReduce
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce
import com.amazonaws.services.elasticmapreduce.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonElasticMapReduce(val context: IacContext) : AbstractAmazonElasticMapReduce(), AmazonElasticMapReduce {


}

class DeferredAmazonElasticMapReduce(context: IacContext) : BaseDeferredAmazonElasticMapReduce(context)
