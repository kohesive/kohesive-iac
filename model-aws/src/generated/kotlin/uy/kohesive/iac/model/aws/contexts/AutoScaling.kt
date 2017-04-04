package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.autoscaling.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface AutoScalingIdentifiable : KohesiveIdentifiable {

}

interface AutoScalingEnabled : AutoScalingIdentifiable {
    val autoScalingClient: AmazonAutoScaling
    val autoScalingContext: AutoScalingContext
    fun <T> withAutoScalingContext(init: AutoScalingContext.(AmazonAutoScaling) -> T): T = autoScalingContext.init(autoScalingClient)
}

open class BaseAutoScalingContext(protected val context: IacContext) : AutoScalingEnabled by context {

    open fun createAutoScalingGroup(autoScalingGroupName: String, init: CreateAutoScalingGroupRequest.() -> Unit): CreateAutoScalingGroupResult {
        return autoScalingClient.createAutoScalingGroup(CreateAutoScalingGroupRequest().apply {
            withAutoScalingGroupName(autoScalingGroupName)
            init()
        })
    }

    open fun createLaunchConfiguration(launchConfigurationName: String, init: CreateLaunchConfigurationRequest.() -> Unit): CreateLaunchConfigurationResult {
        return autoScalingClient.createLaunchConfiguration(CreateLaunchConfigurationRequest().apply {
            withLaunchConfigurationName(launchConfigurationName)
            init()
        })
    }


}

