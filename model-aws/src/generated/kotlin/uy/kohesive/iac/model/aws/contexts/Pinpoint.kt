package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.pinpoint.AmazonPinpoint
import com.amazonaws.services.pinpoint.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface PinpointIdentifiable : KohesiveIdentifiable {

}

interface PinpointEnabled : PinpointIdentifiable {
    val pinpointClient: AmazonPinpoint
    val pinpointContext: PinpointContext
    fun <T> withPinpointContext(init: PinpointContext.(AmazonPinpoint) -> T): T = pinpointContext.init(pinpointClient)
}

open class BasePinpointContext(protected val context: IacContext) : PinpointEnabled by context {


}

@DslScope
class PinpointContext(context: IacContext) : BasePinpointContext(context) {

}
