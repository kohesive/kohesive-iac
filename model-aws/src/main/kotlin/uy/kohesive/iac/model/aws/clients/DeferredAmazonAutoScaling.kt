package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.autoscaling.AbstractAmazonAutoScaling
import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupResult
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationResult
import uy.kohesive.iac.model.aws.IacContext

class DeferredAmazonAutoScaling(val context: IacContext) : AbstractAmazonAutoScaling(), AmazonAutoScaling {

    override fun createAutoScalingGroup(request: CreateAutoScalingGroupRequest): CreateAutoScalingGroupResult {
        return with (context) {
            request.registerWithAutoName()
            CreateAutoScalingGroupResult().registerWithSameNameAs(request)
        }
    }

    override fun createLaunchConfiguration(request: CreateLaunchConfigurationRequest): CreateLaunchConfigurationResult {
        return with (context) {
            request.registerWithAutoName()
            CreateLaunchConfigurationResult().registerWithSameNameAs(request)
        }
    }

}