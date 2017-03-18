package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.applicationautoscaling.AWSApplicationAutoScaling
import com.amazonaws.services.applicationautoscaling.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ApplicationAutoScalingIdentifiable : KohesiveIdentifiable {

}

interface ApplicationAutoScalingEnabled : ApplicationAutoScalingIdentifiable {
    val applicationAutoScalingClient: AWSApplicationAutoScaling
    val applicationAutoScalingContext: ApplicationAutoScalingContext
    fun <T> withApplicationAutoScalingContext(init: ApplicationAutoScalingContext.(AWSApplicationAutoScaling) -> T): T = applicationAutoScalingContext.init(applicationAutoScalingClient)
}

open class BaseApplicationAutoScalingContext(protected val context: IacContext) : ApplicationAutoScalingEnabled by context {

}

@DslScope
class ApplicationAutoScalingContext(context: IacContext) : BaseApplicationAutoScalingContext(context) {

}