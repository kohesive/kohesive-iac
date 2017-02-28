package uy.kohesive.iac.model.aws

import com.amazonaws.services.autoscaling.AbstractAmazonAutoScaling
import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupResult
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationResult

class DeferredAmazonAutoScaling(val context: IacContext) : AbstractAmazonAutoScaling(), AmazonAutoScaling {

    override fun createAutoScalingGroup(request: CreateAutoScalingGroupRequest): CreateAutoScalingGroupResult {
        with (context) {
            return CreateAutoScalingGroupResult().withKohesiveId(getIdStrict(request))
        }
    }

    override fun createLaunchConfiguration(request: CreateLaunchConfigurationRequest): CreateLaunchConfigurationResult {
        with (context) {
            return CreateLaunchConfigurationResult().withKohesiveId(getIdStrict(request))
        }
    }

}