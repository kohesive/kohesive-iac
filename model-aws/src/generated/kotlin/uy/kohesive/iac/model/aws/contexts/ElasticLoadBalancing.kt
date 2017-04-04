package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing
import com.amazonaws.services.elasticloadbalancing.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ElasticLoadBalancingIdentifiable : KohesiveIdentifiable {

}

interface ElasticLoadBalancingEnabled : ElasticLoadBalancingIdentifiable {
    val elasticLoadBalancingClient: AmazonElasticLoadBalancing
    val elasticLoadBalancingContext: ElasticLoadBalancingContext
    fun <T> withElasticLoadBalancingContext(init: ElasticLoadBalancingContext.(AmazonElasticLoadBalancing) -> T): T = elasticLoadBalancingContext.init(elasticLoadBalancingClient)
}

open class BaseElasticLoadBalancingContext(protected val context: IacContext) : ElasticLoadBalancingEnabled by context {

    open fun createLoadBalancer(loadBalancerName: String, init: CreateLoadBalancerRequest.() -> Unit): CreateLoadBalancerResult {
        return elasticLoadBalancingClient.createLoadBalancer(CreateLoadBalancerRequest().apply {
            withLoadBalancerName(loadBalancerName)
            init()
        })
    }


}

@DslScope
class ElasticLoadBalancingContext(context: IacContext) : BaseElasticLoadBalancingContext(context) {

}
