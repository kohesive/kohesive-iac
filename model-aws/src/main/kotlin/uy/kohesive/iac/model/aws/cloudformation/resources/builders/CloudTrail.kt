package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.cloudtrail.model.AddTagsRequest
import com.amazonaws.services.cloudtrail.model.CreateTrailRequest
import com.amazonaws.services.cloudtrail.model.StartLoggingRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudTrail

class CloudTrailTrailResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateTrailRequest> {

    override val requestClazz = CreateTrailRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateTrailRequest).let {
            CloudTrail.Trail(
                CloudWatchLogsLogGroupArn  = request.cloudWatchLogsLogGroupArn,
                CloudWatchLogsRoleArn      = request.cloudWatchLogsRoleArn,
                EnableLogFileValidation    = request.enableLogFileValidation?.toString(),
                IncludeGlobalServiceEvents = request.includeGlobalServiceEvents?.toString(),
                IsMultiRegionTrail         = request.isMultiRegionTrail()?.toString(),
                S3BucketName               = request.s3BucketName,
                S3KeyPrefix                = request.s3KeyPrefix,
                SnsTopicName               = request.snsTopicName,
                KMSKeyId                   = request.kmsKeyId,
                IsLogging                  = relatedObjects.filterIsInstance<StartLoggingRequest>().isNotEmpty().toString(),
                Tags                       = relatedObjects.filterIsInstance<AddTagsRequest>()?.flatMap { it.tagsList }.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

