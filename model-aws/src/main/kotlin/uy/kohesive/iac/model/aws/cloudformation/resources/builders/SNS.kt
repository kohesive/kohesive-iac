package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.sns.model.CreateTopicRequest
import com.amazonaws.services.sns.model.SetTopicAttributesRequest
import com.amazonaws.services.sns.model.SubscribeRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.SNS

class SNSTopicResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateTopicRequest> {

    override val requestClazz = CreateTopicRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateTopicRequest).let {
            SNS.Topic(
                TopicName   = it.name,
                DisplayName = relatedObjects.filterIsInstance<SetTopicAttributesRequest>().filter {
                    it.attributeName == "DisplayName"
                }.lastOrNull()?.attributeValue,
                Subscription = relatedObjects.filterIsInstance<SubscribeRequest>().map {
                    SNS.Topic.SubscriptionProperty(
                        Endpoint = it.endpoint,
                        Protocol = it.protocol
                    )
                }
            )
        }

}

