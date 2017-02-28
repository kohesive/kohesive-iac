package uy.kohesive.iac.model.aws

import com.amazonaws.services.autoscaling.AbstractAmazonAutoScaling
import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupResult
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationResult

class DeferredAmazonAutoScaling(val context: IacContext) : AbstractAmazonAutoScaling(), AmazonAutoScaling {

    override fun createAutoScalingGroup(request: CreateAutoScalingGroupRequest?): CreateAutoScalingGroupResult {
        // TODO: do we need to do anything there? I think not — we've registered the request already
        return CreateAutoScalingGroupResult()
    }

    override fun createLaunchConfiguration(request: CreateLaunchConfigurationRequest?): CreateLaunchConfigurationResult {
        // TODO: do we need to do anything there? I think not — we've registered the request already
        return CreateLaunchConfigurationResult()
    }

}