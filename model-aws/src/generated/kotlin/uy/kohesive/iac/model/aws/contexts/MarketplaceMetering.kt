package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.marketplacemetering.AWSMarketplaceMetering
import com.amazonaws.services.marketplacemetering.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface MarketplaceMeteringIdentifiable : KohesiveIdentifiable {

}

interface MarketplaceMeteringEnabled : MarketplaceMeteringIdentifiable {
    val marketplaceMeteringClient: AWSMarketplaceMetering
    val marketplaceMeteringContext: MarketplaceMeteringContext
    fun <T> withMarketplaceMeteringContext(init: MarketplaceMeteringContext.(AWSMarketplaceMetering) -> T): T = marketplaceMeteringContext.init(marketplaceMeteringClient)
}

open class BaseMarketplaceMeteringContext(protected val context: IacContext) : MarketplaceMeteringEnabled by context {


}

@DslScope
class MarketplaceMeteringContext(context: IacContext) : BaseMarketplaceMeteringContext(context) {

}
