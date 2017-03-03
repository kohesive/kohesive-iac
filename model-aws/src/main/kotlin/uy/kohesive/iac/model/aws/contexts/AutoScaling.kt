package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.autoscaling.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.TagAware
import uy.kohesive.iac.model.aws.utils.DslScope

interface AutoScalingIdentifiable : KohesiveIdentifiable, TagAware<Tag> {

    override fun createTag(key: String, value: String): Tag = Tag().withKey(key).withValue(value)

}

interface AutoScalingEnabled : AutoScalingIdentifiable {
    val autoScalingClient: AmazonAutoScaling
    val autoScalingContext: AutoScalingContext
    fun <T> withAutoScalingContext(init: AutoScalingContext.(AmazonAutoScaling) -> T): T = autoScalingContext.init(autoScalingClient)
}

@DslScope
class AutoScalingContext(private val context: IacContext): AutoScalingEnabled by context {
    val launchConfigTracking = hashMapOf<CreateLaunchConfigurationResult, CreateLaunchConfigurationRequest>()

    fun AutoScalingContext.createLaunchConfiguration(launchConfigurationName: String, init: CreateLaunchConfigurationRequest.() -> Unit): CreateLaunchConfigurationResult {
        val launchConfig = CreateLaunchConfigurationRequest().apply {
            this.launchConfigurationName = launchConfigurationName
            this.init()
            this.registerWithAutoName()
        }
        return autoScalingClient.createLaunchConfiguration(launchConfig).apply {
            launchConfigTracking.put(this, launchConfig)
        }
    }

    val CreateLaunchConfigurationResult.launchConfigurationName: String get() = launchConfigTracking.get(this)!!.launchConfigurationName

    fun AutoScalingContext.createAutoScalingGroup(autoScalingGroupName: String, init: CreateAutoScalingGroupRequest.() -> Unit): CreateAutoScalingGroupResult {
        return autoScalingClient.createAutoScalingGroup(CreateAutoScalingGroupRequest().apply {
            this.autoScalingGroupName = autoScalingGroupName
            this.init()
            this.registerWithAutoName()
        })
    }

}