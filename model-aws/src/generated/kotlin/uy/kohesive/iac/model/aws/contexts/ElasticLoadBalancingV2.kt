package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing
import com.amazonaws.services.elasticloadbalancingv2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ElasticLoadBalancingV2Identifiable : KohesiveIdentifiable {

}

interface ElasticLoadBalancingV2Enabled : ElasticLoadBalancingV2Identifiable {
    val elasticLoadBalancingV2Client: AmazonElasticLoadBalancing
    val elasticLoadBalancingV2Context: ElasticLoadBalancingV2Context
    fun <T> withElasticLoadBalancingV2Context(init: ElasticLoadBalancingV2Context.(AmazonElasticLoadBalancing) -> T): T = elasticLoadBalancingV2Context.init(elasticLoadBalancingV2Client)
}

open class BaseElasticLoadBalancingV2Context(protected val context: IacContext) : ElasticLoadBalancingV2Enabled by context {

    open fun createLoadBalancer(name: String, init: CreateLoadBalancerRequest.() -> Unit): CreateLoadBalancerResult {
        return elasticLoadBalancingV2Client.createLoadBalancer(CreateLoadBalancerRequest().apply {
            withName(name)
            init()
        })
    }

    open fun createTargetGroup(name: String, init: CreateTargetGroupRequest.() -> Unit): CreateTargetGroupResult {
        return elasticLoadBalancingV2Client.createTargetGroup(CreateTargetGroupRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class ElasticLoadBalancingV2Context(context: IacContext) : BaseElasticLoadBalancingV2Context(context) {

}
