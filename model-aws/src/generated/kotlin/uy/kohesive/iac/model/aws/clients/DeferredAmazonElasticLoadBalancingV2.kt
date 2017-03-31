package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticloadbalancingv2.AbstractAmazonElasticLoadBalancing
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing
import com.amazonaws.services.elasticloadbalancingv2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonElasticLoadBalancingV2(val context: IacContext) : AbstractAmazonElasticLoadBalancing(), AmazonElasticLoadBalancing {

    override fun addTags(request: AddTagsRequest): AddTagsResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsResult().registerWithSameNameAs(request)
        }
    }

    override fun createListener(request: CreateListenerRequest): CreateListenerResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateListenerRequest, CreateListenerResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createLoadBalancer(request: CreateLoadBalancerRequest): CreateLoadBalancerResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateLoadBalancerRequest, CreateLoadBalancerResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createRule(request: CreateRuleRequest): CreateRuleResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateRuleRequest, CreateRuleResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createTargetGroup(request: CreateTargetGroupRequest): CreateTargetGroupResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateTargetGroupRequest, CreateTargetGroupResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonElasticLoadBalancingV2(context: IacContext) : BaseDeferredAmazonElasticLoadBalancingV2(context)
