package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticloadbalancing.AbstractAmazonElasticLoadBalancing
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing
import com.amazonaws.services.elasticloadbalancing.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonElasticLoadBalancing(val context: IacContext) : AbstractAmazonElasticLoadBalancing(), AmazonElasticLoadBalancing {

}

class DeferredAmazonElasticLoadBalancing(context: IacContext) : BaseDeferredAmazonElasticLoadBalancing(context)