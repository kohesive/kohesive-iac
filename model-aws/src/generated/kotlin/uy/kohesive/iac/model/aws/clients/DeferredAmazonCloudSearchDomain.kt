package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudsearchdomain.AbstractAmazonCloudSearchDomain
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain
import com.amazonaws.services.cloudsearchdomain.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonCloudSearchDomain(val context: IacContext) : AbstractAmazonCloudSearchDomain(), AmazonCloudSearchDomain {


}

class DeferredAmazonCloudSearchDomain(context: IacContext) : BaseDeferredAmazonCloudSearchDomain(context)
