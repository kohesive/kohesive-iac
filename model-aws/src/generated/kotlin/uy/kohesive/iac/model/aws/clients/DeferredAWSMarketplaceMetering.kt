package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.marketplacemetering.AbstractAWSMarketplaceMetering
import com.amazonaws.services.marketplacemetering.AWSMarketplaceMetering
import com.amazonaws.services.marketplacemetering.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSMarketplaceMetering(val context: IacContext) : AbstractAWSMarketplaceMetering(), AWSMarketplaceMetering {

}

class DeferredAWSMarketplaceMetering(context: IacContext) : BaseDeferredAWSMarketplaceMetering(context)