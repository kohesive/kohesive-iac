package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.applicationautoscaling.model.PutScalingPolicyRequest
import com.amazonaws.services.applicationautoscaling.model.RegisterScalableTargetRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.ApplicationAutoScaling

class ApplicationAutoScalingScalableTargetResourcePropertiesBuilder : ResourcePropertiesBuilder<RegisterScalableTargetRequest> {

    override val requestClazz = RegisterScalableTargetRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as RegisterScalableTargetRequest).let {
            ApplicationAutoScaling.ScalableTarget(
                MaxCapacity       = it.maxCapacity.toString(),
                MinCapacity       = it.minCapacity.toString(),
                ResourceId        = it.resourceId,
                RoleARN           = it.roleARN,
                ScalableDimension = it.scalableDimension,
                ServiceNamespace  = it.serviceNamespace
            )
        }
}

class ApplicationAutoScalingScalingPolicyResourcePropertiesBuilder : ResourcePropertiesBuilder<PutScalingPolicyRequest> {

    override val requestClazz = PutScalingPolicyRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutScalingPolicyRequest).let {
            ApplicationAutoScaling.ScalingPolicy(
                PolicyName                     = request.policyName,
                PolicyType                     = request.policyType,
                ResourceId                     = request.resourceId,
                ScalableDimension              = request.scalableDimension,
                ServiceNamespace               = request.serviceNamespace,
                StepScalingPolicyConfiguration = request.stepScalingPolicyConfiguration?.let {
                    ApplicationAutoScaling.ScalingPolicy.StepScalingPolicyConfigurationProperty(
                        AdjustmentType         = it.adjustmentType,
                        Cooldown               = it.cooldown?.toString(),
                        MetricAggregationType  = it.metricAggregationType,
                        MinAdjustmentMagnitude = it.minAdjustmentMagnitude?.toString(),
                        StepAdjustments        = it.stepAdjustments?.map {
                            ApplicationAutoScaling.ScalingPolicy.StepScalingPolicyConfigurationProperty.StepAdjustmentProperty(
                                MetricIntervalLowerBound = it.metricIntervalLowerBound?.toString(),
                                MetricIntervalUpperBound = it.metricIntervalUpperBound?.toString(),
                                ScalingAdjustment        = it.scalingAdjustment.toString()
                            )
                        }
                    )
                }
            )
        }

}

