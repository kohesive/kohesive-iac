package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticloadbalancingv2.AbstractAmazonElasticLoadBalancing
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonElasticLoadBalancingV2(val context: IacContext) : AbstractAmazonElasticLoadBalancing(), AmazonElasticLoadBalancing {


}

class DeferredAmazonElasticLoadBalancingV2(context: IacContext) : BaseDeferredAmazonElasticLoadBalancingV2(context)
