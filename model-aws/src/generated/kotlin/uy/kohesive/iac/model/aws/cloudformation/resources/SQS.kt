package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes
import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties

@CloudFormationTypes
object SQS {

    @CloudFormationType("AWS::SQS::Queue")
    data class Queue(
        val DelaySeconds: String? = null,
        val MaximumMessageSize: String? = null,
        val MessageRetentionPeriod: String? = null,
        val QueueName: String? = null,
        val ReceiveMessageWaitTimeSeconds: String? = null,
        val RedrivePolicy: Queue.RedrivePolicyProperty? = null,
        val VisibilityTimeout: String? = null
    ) : ResourceProperties {

        data class RedrivePolicyProperty(
            val deadLetterTargetArn: String?,
            val maxReceiveCount: String?
        ) 

    }

    @CloudFormationType("AWS::SQS::QueuePolicy")
    data class QueuePolicy(
        val PolicyDocument: Any,
        val Queues: List<String>
    ) : ResourceProperties 


}