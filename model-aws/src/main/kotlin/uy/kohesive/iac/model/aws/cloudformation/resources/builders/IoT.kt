package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.iot.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.IoT

class IoTTopicRuleResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateTopicRuleRequest> {

    override val requestClazz = CreateTopicRuleRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateTopicRuleRequest).let {
            IoT.TopicRule(
                RuleName         = request.ruleName,
                TopicRulePayload = request.topicRulePayload.let {
                    IoT.TopicRule.TopicRulePayloadProperty(
                        Actions = it.actions.map {
                            IoT.TopicRule.TopicRulePayloadProperty.ActionProperty(
                                CloudwatchAlarm = it.cloudwatchAlarm?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.CloudwatchAlarmActionProperty(
                                        AlarmName   = it.alarmName,
                                        RoleArn     = it.roleArn,
                                        StateReason = it.stateReason,
                                        StateValue  = it.stateValue
                                    )
                                },
                                CloudwatchMetric = it.cloudwatchMetric?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.CloudwatchMetricActionProperty(
                                        MetricName      = it.metricName,
                                        MetricNamespace = it.metricNamespace,
                                        MetricTimestamp = it.metricTimestamp,
                                        MetricUnit      = it.metricUnit,
                                        MetricValue     = it.metricValue,
                                        RoleArn         = it.roleArn
                                    )
                                },
                                DynamoDB = it.dynamoDB?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.DynamoDBActionProperty(
                                        HashKeyField  = it.hashKeyField,
                                        HashKeyValue  = it.hashKeyValue,
                                        RoleArn       = it.roleArn,
                                        TableName     = it.tableName,
                                        PayloadField  = it.payloadField,
                                        RangeKeyField = it.rangeKeyField,
                                        RangeKeyValue = it.rangeKeyValue
                                    )
                                },
                                Elasticsearch = it.elasticsearch?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.ElasticsearchActionProperty(
                                        Endpoint = it.endpoint,
                                        RoleArn  = it.roleArn,
                                        Type     = it.type,
                                        Id       = it.id,
                                        Index    = it.index
                                    )
                                },
                                Firehose = it.firehose?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.FirehoseActionProperty(
                                        DeliveryStreamName = it.deliveryStreamName,
                                        RoleArn            = it.roleArn,
                                        Separator          = it.separator
                                    )
                                },
                                Kinesis = it.kinesis?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.KinesisActionProperty(
                                        PartitionKey = it.partitionKey,
                                        RoleArn      = it.roleArn,
                                        StreamName   = it.streamName
                                    )
                                },
                                Lambda = it.lambda?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.LambdaActionProperty(
                                        FunctionArn = it.functionArn
                                    )
                                },
                                Republish = it.republish?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.RepublishActionProperty(
                                        RoleArn = it.roleArn,
                                        Topic   = it.topic
                                    )
                                },
                                S3 = it.s3?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.S3ActionProperty(
                                        BucketName = it.bucketName,
                                        RoleArn    = it.roleArn,
                                        Key        = it.key
                                    )
                                },
                                Sns = it.sns?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.SnsActionProperty(
                                        MessageFormat = it.messageFormat,
                                        RoleArn       = it.roleArn,
                                        TargetArn     = it.targetArn
                                    )
                                },
                                Sqs = it.sqs?.let {
                                    IoT.TopicRule.TopicRulePayloadProperty.ActionProperty.SqsActionProperty(
                                        QueueUrl  = it.queueUrl,
                                        RoleArn   = it.roleArn,
                                        UseBase64 = it.useBase64?.toString()
                                    )
                                }
                            )
                        },
                        Description      = it.description,
                        AwsIotSqlVersion = it.awsIotSqlVersion,
                        RuleDisabled     = it.ruleDisabled.toString(),
                        Sql              = it.sql
                    )
                }
            )
        }
}

class IoTPolicyPrincipalAttachmentResourcePropertiesBuilder : ResourcePropertiesBuilder<AttachPrincipalPolicyRequest> {

    override val requestClazz = AttachPrincipalPolicyRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as AttachPrincipalPolicyRequest).let {
            IoT.PolicyPrincipalAttachment(
                PolicyName = it.policyName,
                Principal  = it.principal
            )
        }
}

class IoTThingPrincipalAttachmentResourcePropertiesBuilder : ResourcePropertiesBuilder<AttachThingPrincipalRequest> {

    override val requestClazz = AttachThingPrincipalRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as AttachThingPrincipalRequest).let {
            IoT.ThingPrincipalAttachment(
                ThingName = it.thingName,
                Principal = it.principal
            )
        }
}

class IoTCertificateResourcePropertiesBuilder : ResourcePropertiesBuilder<RegisterCertificateRequest> {

    override val requestClazz = RegisterCertificateRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as RegisterCertificateRequest).let {
            IoT.Certificate(
                Status                    = request.status,
                CertificateSigningRequest = request.certificatePem
            )
        }

}

class IoTPolicyResourcePropertiesBuilder : ResourcePropertiesBuilder<CreatePolicyRequest> {

    override val requestClazz = CreatePolicyRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreatePolicyRequest).let {
            IoT.Policy(
                PolicyDocument = request.policyDocument,
                PolicyName     = request.policyName
            )
        }

}

class IoTThingResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateThingRequest> {

    override val requestClazz = CreateThingRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateThingRequest).let {
            IoT.Thing(
                AttributePayload = request.attributePayload?.attributes,
                ThingName        = request.thingName
            )
        }

}

