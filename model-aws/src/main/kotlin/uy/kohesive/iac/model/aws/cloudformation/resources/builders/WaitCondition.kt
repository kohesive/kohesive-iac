package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.wait.CreateWaitConditionRequest

class WaitConditionPropertiesBuilder : ResourcePropertiesBuilder<CreateWaitConditionRequest> {

    override val requestClazz = CreateWaitConditionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateWaitConditionRequest).let {
            CloudFormation.WaitCondition(
                Handle  = it.handle,
                Timeout = it.timeout.toString(),
                Count   = it.count?.toString()
            )
        }
}
