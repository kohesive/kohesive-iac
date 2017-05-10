package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.logs.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.Logs

class LogsDestinationResourcePropertiesBuilder : ResourcePropertiesBuilder<PutDestinationRequest> {

    override val requestClazz = PutDestinationRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutDestinationRequest).let {
            Logs.Destination(
                DestinationName   = request.destinationName,
                RoleArn           = request.roleArn,
                TargetArn         = request.targetArn,
                DestinationPolicy = relatedObjects.filterIsInstance<PutDestinationPolicyRequest>().last().accessPolicy
            )
        }

}

class LogsLogGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateLogGroupRequest> {

    override val requestClazz = CreateLogGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateLogGroupRequest).let {
            Logs.LogGroup(
                LogGroupName    = request.logGroupName,
                RetentionInDays = relatedObjects.filterIsInstance<PutRetentionPolicyRequest>().lastOrNull()?.retentionInDays?.toString()
            )
        }

}

class LogsLogStreamResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateLogStreamRequest> {

    override val requestClazz = CreateLogStreamRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateLogStreamRequest).let {
            Logs.LogStream(
                LogGroupName = request.logGroupName,
                LogStreamName = request.logStreamName
            )
        }

}

class LogsMetricFilterResourcePropertiesBuilder : ResourcePropertiesBuilder<PutMetricFilterRequest> {

    override val requestClazz = PutMetricFilterRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutMetricFilterRequest).let {
            Logs.MetricFilter(
                FilterPattern         = request.filterPattern,
                LogGroupName          = request.logGroupName,
                MetricTransformations = request.metricTransformations.map {
                    Logs.MetricFilter.MetricTransformationProperty(
                        MetricName      = it.metricName,
                        MetricNamespace = it.metricNamespace,
                        MetricValue     = it.metricValue
                    )
                }
            )
        }

}

class LogsSubscriptionFilterResourcePropertiesBuilder : ResourcePropertiesBuilder<PutSubscriptionFilterRequest> {

    override val requestClazz = PutSubscriptionFilterRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutSubscriptionFilterRequest).let {
            Logs.SubscriptionFilter(
                DestinationArn = request.destinationArn,
                FilterPattern  = request.filterPattern,
                LogGroupName   = request.logGroupName,
                RoleArn        = request.roleArn
            )
        }

}

