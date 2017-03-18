package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.route53.AbstractAmazonRoute53
import com.amazonaws.services.route53.AmazonRoute53
import com.amazonaws.services.route53.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonRoute53(val context: IacContext) : AbstractAmazonRoute53(), AmazonRoute53 {

}

class DeferredAmazonRoute53(context: IacContext) : BaseDeferredAmazonRoute53(context)