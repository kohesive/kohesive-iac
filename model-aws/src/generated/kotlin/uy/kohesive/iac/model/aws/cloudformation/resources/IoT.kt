package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object IoT {

    @CloudFormationType("AWS::IoT::Certificate")
    data class Certificate(
        val CertificateSigningRequest: String,
        val Status: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::IoT::Policy")
    data class Policy(
        val PolicyDocument: Any,
        val PolicyName: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::IoT::PolicyPrincipalAttachment")
    data class PolicyPrincipalAttachment(
        val PolicyName: String,
        val Principal: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::IoT::Thing")
    data class Thing(
        val AttributePayload: Map<String, String>? = null,
        val ThingName: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::IoT::ThingPrincipalAttachment")
    data class ThingPrincipalAttachment(
        val Principal: String,
        val ThingName: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::IoT::TopicRule")
    data class TopicRule(
        val RuleName: String? = null,
        val TopicRulePayload: TopicRule.TopicRulePayloadProperty
    ) : ResourceProperties {

        data class TopicRulePayloadProperty(
            val Actions: List<IoT.TopicRule.TopicRulePayloadProperty.ActionProperty>,
            val AwsIotSqlVersion: String? = null,
            val Description: String? = null,
            val RuleDisabled: String,
            val Sql: String
        ) {

            data class ActionProperty(
                val CloudwatchAlarm: TopicRule.TopicRulePayloadProperty.ActionProperty.CloudwatchAlarmActionProperty? = null,
                val CloudwatchMetric: TopicRule.TopicRulePayloadProperty.ActionProperty.CloudwatchMetricActionProperty? = null,
                val DynamoDB: TopicRule.TopicRulePayloadProperty.ActionProperty.DynamoDBActionProperty? = null,
                val Elasticsearch: TopicRule.TopicRulePayloadProperty.ActionProperty.ElasticsearchActionProperty? = null,
                val Firehose: TopicRule.TopicRulePayloadProperty.ActionProperty.FirehoseActionProperty? = null,
                val Kinesis: TopicRule.TopicRulePayloadProperty.ActionProperty.KinesisActionProperty? = null,
                val Lambda: TopicRule.TopicRulePayloadProperty.ActionProperty.LambdaActionProperty? = null,
                val Republish: TopicRule.TopicRulePayloadProperty.ActionProperty.RepublishActionProperty? = null,
                val S3: TopicRule.TopicRulePayloadProperty.ActionProperty.S3ActionProperty? = null,
                val Sns: TopicRule.TopicRulePayloadProperty.ActionProperty.SnsActionProperty? = null,
                val Sqs: TopicRule.TopicRulePayloadProperty.ActionProperty.SqsActionProperty? = null
            ) {

                data class CloudwatchAlarmActionProperty(
                    val AlarmName: String,
                    val RoleArn: String,
                    val StateReason: String,
                    val StateValue: String
                ) 


                data class CloudwatchMetricActionProperty(
                    val MetricName: String,
                    val MetricNamespace: String,
                    val MetricTimestamp: String? = null,
                    val MetricUnit: String,
                    val MetricValue: String,
                    val RoleArn: String
                ) 


                data class DynamoDBActionProperty(
                    val HashKeyField: String,
                    val HashKeyValue: String,
                    val PayloadField: String? = null,
                    val RangeKeyField: String,
                    val RangeKeyValue: String,
                    val RoleArn: String,
                    val TableName: String
                ) 


                data class ElasticsearchActionProperty(
                    val Endpoint: String,
                    val Id: String,
                    val Index: String,
                    val RoleArn: String,
                    val Type: String
                ) 


                data class FirehoseActionProperty(
                    val DeliveryStreamName: String,
                    val RoleArn: String,
                    val Separator: String? = null
                ) 


                data class KinesisActionProperty(
                    val PartitionKey: String? = null,
                    val RoleArn: String,
                    val StreamName: String
                ) 


                data class LambdaActionProperty(
                    val FunctionArn: String
                ) 


                data class RepublishActionProperty(
                    val RoleArn: String,
                    val Topic: String
                ) 


                data class S3ActionProperty(
                    val BucketName: String,
                    val Key: String,
                    val RoleArn: String
                ) 


                data class SnsActionProperty(
                    val MessageFormat: String? = null,
                    val RoleArn: String,
                    val TargetArn: String
                ) 


                data class SqsActionProperty(
                    val QueueUrl: String,
                    val RoleArn: String,
                    val UseBase64: String? = null
                ) 

            }

        }

    }


}