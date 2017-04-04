package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce
import com.amazonaws.services.elasticmapreduce.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ElasticMapReduceIdentifiable : KohesiveIdentifiable {

}

interface ElasticMapReduceEnabled : ElasticMapReduceIdentifiable {
    val elasticMapReduceClient: AmazonElasticMapReduce
    val elasticMapReduceContext: ElasticMapReduceContext
    fun <T> withElasticMapReduceContext(init: ElasticMapReduceContext.(AmazonElasticMapReduce) -> T): T = elasticMapReduceContext.init(elasticMapReduceClient)
}

open class BaseElasticMapReduceContext(protected val context: IacContext) : ElasticMapReduceEnabled by context {

    open fun createSecurityConfiguration(name: String, init: CreateSecurityConfigurationRequest.() -> Unit): CreateSecurityConfigurationResult {
        return elasticMapReduceClient.createSecurityConfiguration(CreateSecurityConfigurationRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class ElasticMapReduceContext(context: IacContext) : BaseElasticMapReduceContext(context) {

}
