package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.kinesis.model.AddTagsToStreamRequest
import com.amazonaws.services.kinesis.model.CreateStreamRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.Kinesis

class KinesisStreamResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateStreamRequest> {

    override val requestClazz = CreateStreamRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateStreamRequest).let {
            Kinesis.Stream(
                ShardCount = request.shardCount.toString(),
                Name       = it.streamName,
                Tags       = relatedObjects.filterIsInstance<AddTagsToStreamRequest>().flatMap { it.tags.toList() }.map {
                    CloudFormation.ResourceTag(
                        Key   = it.first,
                        Value = it.second
                    )
                }
            )
        }

}

