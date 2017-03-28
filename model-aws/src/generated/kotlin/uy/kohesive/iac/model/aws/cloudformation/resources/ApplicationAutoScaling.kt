package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object ApplicationAutoScaling {

    @CloudFormationType("AWS::ApplicationAutoScaling::ScalableTarget")
    data class ScalableTarget(
        val MaxCapacity: String,
        val MinCapacity: String,
        val ResourceId: String,
        val RoleARN: String,
        val ScalableDimension: String,
        val ServiceNamespace: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::ApplicationAutoScaling::ScalingPolicy")
    data class ScalingPolicy(
        val PolicyName: String,
        val PolicyType: String,
        val ResourceId: String? = null,
        val ScalableDimension: String? = null,
        val ServiceNamespace: String? = null,
        val ScalingTargetId: String? = null,
        val StepScalingPolicyConfiguration: ScalingPolicy.StepScalingPolicyConfigurationProperty? = null
    ) : ResourceProperties {

        data class StepScalingPolicyConfigurationProperty(
            val AdjustmentType: String? = null,
            val Cooldown: String? = null,
            val MetricAggregationType: String? = null,
            val MinAdjustmentMagnitude: String? = null,
            val StepAdjustments: List<ApplicationAutoScaling.ScalingPolicy.StepScalingPolicyConfigurationProperty.StepAdjustmentProperty>? = null
        ) {

            data class StepAdjustmentProperty(
                val MetricIntervalLowerBound: String? = null,
                val MetricIntervalUpperBound: String? = null,
                val ScalingAdjustment: String
            ) 

        }

    }


}