package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudtrail.AbstractAWSCloudTrail
import com.amazonaws.services.cloudtrail.AWSCloudTrail
import com.amazonaws.services.cloudtrail.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCloudTrail(val context: IacContext) : AbstractAWSCloudTrail(), AWSCloudTrail {

    override fun addTags(request: AddTagsRequest): AddTagsResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsResult().registerWithSameNameAs(request)
        }
    }

    override fun createTrail(request: CreateTrailRequest): CreateTrailResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateTrailRequest, CreateTrailResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateTrailRequest::getName to CreateTrailResult::getName,
                    CreateTrailRequest::getS3BucketName to CreateTrailResult::getS3BucketName,
                    CreateTrailRequest::getS3KeyPrefix to CreateTrailResult::getS3KeyPrefix,
                    CreateTrailRequest::getSnsTopicName to CreateTrailResult::getSnsTopicName,
                    CreateTrailRequest::getIncludeGlobalServiceEvents to CreateTrailResult::getIncludeGlobalServiceEvents,
                    CreateTrailRequest::getIsMultiRegionTrail to CreateTrailResult::getIsMultiRegionTrail,
                    CreateTrailRequest::getCloudWatchLogsLogGroupArn to CreateTrailResult::getCloudWatchLogsLogGroupArn,
                    CreateTrailRequest::getCloudWatchLogsRoleArn to CreateTrailResult::getCloudWatchLogsRoleArn,
                    CreateTrailRequest::getKmsKeyId to CreateTrailResult::getKmsKeyId
                )
            )
        }
    }


}

class DeferredAWSCloudTrail(context: IacContext) : BaseDeferredAWSCloudTrail(context)
