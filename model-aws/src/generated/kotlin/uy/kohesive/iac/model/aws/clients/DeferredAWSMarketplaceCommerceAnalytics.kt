package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.marketplacecommerceanalytics.AbstractAWSMarketplaceCommerceAnalytics
import com.amazonaws.services.marketplacecommerceanalytics.AWSMarketplaceCommerceAnalytics
import com.amazonaws.services.marketplacecommerceanalytics.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSMarketplaceCommerceAnalytics(val context: IacContext) : AbstractAWSMarketplaceCommerceAnalytics(), AWSMarketplaceCommerceAnalytics {


}

class DeferredAWSMarketplaceCommerceAnalytics(context: IacContext) : BaseDeferredAWSMarketplaceCommerceAnalytics(context)
