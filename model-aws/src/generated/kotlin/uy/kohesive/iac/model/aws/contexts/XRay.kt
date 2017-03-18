package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.xray.AWSXRay
import com.amazonaws.services.xray.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface XRayIdentifiable : KohesiveIdentifiable {

}

interface XRayEnabled : XRayIdentifiable {
    val xRayClient: AWSXRay
    val xRayContext: XRayContext
    fun <T> withXRayContext(init: XRayContext.(AWSXRay) -> T): T = xRayContext.init(xRayClient)
}

open class BaseXRayContext(protected val context: IacContext) : XRayEnabled by context {

}

@DslScope
class XRayContext(context: IacContext) : BaseXRayContext(context) {

}