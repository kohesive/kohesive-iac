package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.autoscaling.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.utils.DslScope

@DslScope
class AutoScalingContext(context: IacContext): BaseAutoScalingContext(context) {

    fun createTag(key: String, value: String): Tag = Tag().withKey(key).withValue(value)

    val launchConfigTracking = hashMapOf<CreateLaunchConfigurationResult, CreateLaunchConfigurationRequest>()

    fun AutoScalingContext.createLaunchConfiguration(launchConfigurationName: String, init: CreateLaunchConfigurationRequest.() -> Unit): CreateLaunchConfigurationResult {
        val launchConfig = CreateLaunchConfigurationRequest().apply {
            this.launchConfigurationName = launchConfigurationName
            this.init()
        }
        return autoScalingClient.createLaunchConfiguration(launchConfig).apply {
            launchConfigTracking.put(this, launchConfig)
        }
    }

    val CreateLaunchConfigurationResult.launchConfigurationName: String get() = launchConfigTracking[this]!!.launchConfigurationName

    fun AutoScalingContext.createAutoScalingGroup(autoScalingGroupName: String, init: CreateAutoScalingGroupRequest.() -> Unit): CreateAutoScalingGroupResult {
        return autoScalingClient.createAutoScalingGroup(CreateAutoScalingGroupRequest().apply {
            this.autoScalingGroupName = autoScalingGroupName
            this.init()
        })
    }

}