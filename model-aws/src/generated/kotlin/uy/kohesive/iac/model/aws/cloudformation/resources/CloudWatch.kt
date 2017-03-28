package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object CloudWatch {

    @CloudFormationType("AWS::CloudWatch::Alarm")
    data class Alarm(
        val ActionsEnabled: String? = null,
        val AlarmActions: List<String>? = null,
        val AlarmDescription: String? = null,
        val AlarmName: String? = null,
        val ComparisonOperator: String,
        val Dimensions: List<CloudWatch.Alarm.DimensionProperty>? = null,
        val EvaluationPeriods: String,
        val InsufficientDataActions: List<String>? = null,
        val MetricName: String,
        val Namespace: String,
        val OKActions: List<String>? = null,
        val Period: String,
        val Statistic: String,
        val Threshold: String,
        val Unit: String? = null
    ) : ResourceProperties {

        data class DimensionProperty(
            val Name: String,
            val Value: String
        ) 

    }


}