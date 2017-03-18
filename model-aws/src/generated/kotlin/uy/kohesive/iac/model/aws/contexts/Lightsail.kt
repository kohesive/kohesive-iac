package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.lightsail.AmazonLightsail
import com.amazonaws.services.lightsail.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface LightsailIdentifiable : KohesiveIdentifiable {

}

interface LightsailEnabled : LightsailIdentifiable {
    val lightsailClient: AmazonLightsail
    val lightsailContext: LightsailContext
    fun <T> withLightsailContext(init: LightsailContext.(AmazonLightsail) -> T): T = lightsailContext.init(lightsailClient)
}

open class BaseLightsailContext(protected val context: IacContext) : LightsailEnabled by context {

}

@DslScope
class LightsailContext(context: IacContext) : BaseLightsailContext(context) {

}