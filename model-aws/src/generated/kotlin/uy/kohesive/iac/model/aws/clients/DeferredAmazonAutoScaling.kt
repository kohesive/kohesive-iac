package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.autoscaling.AbstractAmazonAutoScaling
import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.autoscaling.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonAutoScaling(val context: IacContext) : AbstractAmazonAutoScaling(), AmazonAutoScaling {

    override fun attachInstances(request: AttachInstancesRequest): AttachInstancesResult {
        return with (context) {
            request.registerWithAutoName()
            AttachInstancesResult().registerWithSameNameAs(request)
        }
    }

    override fun attachLoadBalancerTargetGroups(request: AttachLoadBalancerTargetGroupsRequest): AttachLoadBalancerTargetGroupsResult {
        return with (context) {
            request.registerWithAutoName()
            AttachLoadBalancerTargetGroupsResult().registerWithSameNameAs(request)
        }
    }

    override fun attachLoadBalancers(request: AttachLoadBalancersRequest): AttachLoadBalancersResult {
        return with (context) {
            request.registerWithAutoName()
            AttachLoadBalancersResult().registerWithSameNameAs(request)
        }
    }

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

