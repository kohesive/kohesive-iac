package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object Logs {

    @CloudFormationType("AWS::Logs::Destination")
    data class Destination(
        val DestinationName: String,
        val DestinationPolicy: String,
        val RoleArn: String,
        val TargetArn: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::Logs::LogGroup")
    data class LogGroup(
        val LogGroupName: String? = null,
        val RetentionInDays: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::Logs::LogStream")
    data class LogStream(
        val LogGroupName: String,
        val LogStreamName: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::Logs::MetricFilter")
    data class MetricFilter(
        val FilterPattern: String,
        val LogGroupName: String,
        val MetricTransformations: List<Logs.MetricFilter.MetricTransformationProperty>
    ) : ResourceProperties {

        data class MetricTransformationProperty(
            val MetricName: String,
            val MetricNamespace: String,
            val MetricValue: String
        ) 

    }

    @CloudFormationType("AWS::Logs::SubscriptionFilter")
    data class SubscriptionFilter(
        val DestinationArn: String,
        val FilterPattern: String,
        val LogGroupName: String,
        val RoleArn: String? = null
    ) : ResourceProperties 


}