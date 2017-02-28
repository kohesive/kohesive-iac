package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.autoscaling.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.TagAware
import uy.kohesive.iac.model.aws.utils.DslScope

interface AutoScalingIdentifiable : KohesiveIdentifiable, TagAware<Tag> {

    override fun createTag(key: String, value: String): Tag = Tag().withKey(key).withValue(value)

    fun CreateAutoScalingGroupRequest.withKohesiveIdFromName(): CreateAutoScalingGroupRequest = apply {
        withKohesiveId(this.autoScalingGroupName)
    }

    fun CreateLaunchConfigurationRequest.withKohesiveIdFromName(): CreateLaunchConfigurationRequest = apply {
        withKohesiveId(this.launchConfigurationName)
    }

}

interface AutoScalingEnabled : AutoScalingIdentifiable {
    val autoScalingClient: AmazonAutoScaling
    val autoScalingContext: AutoScalingContext
    fun <T> withAutoScalingContext(init: AutoScalingContext.(AmazonAutoScaling) -> T): T = autoScalingContext.init(autoScalingClient)
}

@DslScope
class AutoScalingContext(private val context: IacContext): AutoScalingEnabled by context {

    fun AutoScalingContext.createLaunchConfiguration(init: CreateLaunchConfigurationRequest.() -> Unit): CreateLaunchConfigurationResult {
        return autoScalingClient.createLaunchConfiguration(CreateLaunchConfigurationRequest().apply {
            init()
            withKohesiveIdFromName()
        })
    }

    fun AutoScalingContext.createAutoScalingGroup(init: CreateAutoScalingGroupRequest.() -> Unit): CreateAutoScalingGroupResult {
        return autoScalingClient.createAutoScalingGroup(CreateAutoScalingGroupRequest().apply {
            init()
            withKohesiveIdFromName()
        })
    }

}