package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticloadbalancing.AbstractAmazonElasticLoadBalancing
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing
import com.amazonaws.services.elasticloadbalancing.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonElasticLoadBalancing(val context: IacContext) : AbstractAmazonElasticLoadBalancing(), AmazonElasticLoadBalancing {

    override fun addTags(request: AddTagsRequest): AddTagsResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsResult().registerWithSameNameAs(request)
        }
    }

    override fun attachLoadBalancerToSubnets(request: AttachLoadBalancerToSubnetsRequest): AttachLoadBalancerToSubnetsResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AttachLoadBalancerToSubnetsRequest, AttachLoadBalancerToSubnetsResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    AttachLoadBalancerToSubnetsRequest::getSubnets to AttachLoadBalancerToSubnetsResult::getSubnets
                )
            )
        }
    }

    override fun createAppCookieStickinessPolicy(request: CreateAppCookieStickinessPolicyRequest): CreateAppCookieStickinessPolicyResult {
        return with (context) {
            request.registerWithAutoName()
            CreateAppCookieStickinessPolicyResult().registerWithSameNameAs(request)
        }
    }

    override fun createLBCookieStickinessPolicy(request: CreateLBCookieStickinessPolicyRequest): CreateLBCookieStickinessPolicyResult {
        return with (context) {
            request.registerWithAutoName()
            CreateLBCookieStickinessPolicyResult().registerWithSameNameAs(request)
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

    override fun createLoadBalancerListeners(request: CreateLoadBalancerListenersRequest): CreateLoadBalancerListenersResult {
        return with (context) {
            request.registerWithAutoName()
            CreateLoadBalancerListenersResult().registerWithSameNameAs(request)
        }
    }

    override fun createLoadBalancerPolicy(request: CreateLoadBalancerPolicyRequest): CreateLoadBalancerPolicyResult {
        return with (context) {
            request.registerWithAutoName()
            CreateLoadBalancerPolicyResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonElasticLoadBalancing(context: IacContext) : BaseDeferredAmazonElasticLoadBalancing(context)
