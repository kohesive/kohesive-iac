package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.marketplacecommerceanalytics.AWSMarketplaceCommerceAnalytics
import com.amazonaws.services.marketplacecommerceanalytics.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface MarketplaceCommerceAnalyticsIdentifiable : KohesiveIdentifiable {

}

interface MarketplaceCommerceAnalyticsEnabled : MarketplaceCommerceAnalyticsIdentifiable {
    val marketplaceCommerceAnalyticsClient: AWSMarketplaceCommerceAnalytics
    val marketplaceCommerceAnalyticsContext: MarketplaceCommerceAnalyticsContext
    fun <T> withMarketplaceCommerceAnalyticsContext(init: MarketplaceCommerceAnalyticsContext.(AWSMarketplaceCommerceAnalytics) -> T): T = marketplaceCommerceAnalyticsContext.init(marketplaceCommerceAnalyticsClient)
}

open class BaseMarketplaceCommerceAnalyticsContext(protected val context: IacContext) : MarketplaceCommerceAnalyticsEnabled by context {


}

@DslScope
class MarketplaceCommerceAnalyticsContext(context: IacContext) : BaseMarketplaceCommerceAnalyticsContext(context) {

}
