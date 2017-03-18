package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.autoscaling.AmazonAutoScaling
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable

interface AutoScalingIdentifiable : KohesiveIdentifiable {

}

interface AutoScalingEnabled : AutoScalingIdentifiable {
    val autoScalingClient: AmazonAutoScaling
    val autoScalingContext: AutoScalingContext
    fun <T> withAutoScalingContext(init: AutoScalingContext.(AmazonAutoScaling) -> T): T = autoScalingContext.init(autoScalingClient)
}

open class BaseAutoScalingContext(protected val context: IacContext) : AutoScalingEnabled by context {

}
