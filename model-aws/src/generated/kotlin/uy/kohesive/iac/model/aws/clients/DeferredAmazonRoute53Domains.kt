package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.route53domains.AbstractAmazonRoute53Domains
import com.amazonaws.services.route53domains.AmazonRoute53Domains
import com.amazonaws.services.route53domains.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonRoute53Domains(val context: IacContext) : AbstractAmazonRoute53Domains(), AmazonRoute53Domains {


}

class DeferredAmazonRoute53Domains(context: IacContext) : BaseDeferredAmazonRoute53Domains(context)
