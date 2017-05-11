package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.SQS
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class SQSQueueResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateQueueRequest> {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    override val requestClazz = CreateQueueRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateQueueRequest).let {
            SQS.Queue(
                QueueName                     = request.queueName,
                DelaySeconds                  = request.attributes["DelaySeconds"],
                MaximumMessageSize            = request.attributes["MaximumMessageSize"],
                MessageRetentionPeriod        = request.attributes["MessageRetentionPeriod"],
                ReceiveMessageWaitTimeSeconds = request.attributes["ReceiveMessageWaitTimeSeconds"],
                RedrivePolicy                 = request.attributes["RedrivePolicy"]?.let { redrivePolicyJSON ->
                    val redrivePolicyMap = JSON.readValue<Map<String, String>>(redrivePolicyJSON)
                    SQS.Queue.RedrivePolicyProperty(
                        deadLetterTargetArn = redrivePolicyMap["deadLetterTargetArn"],
                        maxReceiveCount     = redrivePolicyMap["maxReceiveCount"]
                    )
                },
                VisibilityTimeout = request.attributes["VisibilityTimeout"]
            )
        }

}

