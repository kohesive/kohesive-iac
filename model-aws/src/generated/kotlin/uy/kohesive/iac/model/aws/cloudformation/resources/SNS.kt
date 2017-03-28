package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object SNS {

    @CloudFormationType("AWS::SNS::Subscription")
    data class Subscription(
        val Endpoint: String? = null,
        val Protocol: String? = null,
        val TopicArn: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::SNS::Topic")
    data class Topic(
        val DisplayName: String? = null,
        val Subscription: List<SNS.Topic.SubscriptionProperty>? = null,
        val TopicName: String? = null
    ) : ResourceProperties {

        data class SubscriptionProperty(
            val Endpoint: String,
            val Protocol: String
        ) 

    }

    @CloudFormationType("AWS::SNS::TopicPolicy")
    data class TopicPolicy(
        val PolicyDocument: Any,
        val Topics: List<String>
    ) : ResourceProperties 


}