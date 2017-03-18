package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.lightsail.AbstractAmazonLightsail
import com.amazonaws.services.lightsail.AmazonLightsail
import com.amazonaws.services.lightsail.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonLightsail(val context: IacContext) : AbstractAmazonLightsail(), AmazonLightsail {

}

class DeferredAmazonLightsail(context: IacContext) : BaseDeferredAmazonLightsail(context)