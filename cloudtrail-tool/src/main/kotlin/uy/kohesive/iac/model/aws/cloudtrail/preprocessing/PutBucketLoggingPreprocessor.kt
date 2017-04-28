package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailEvent

class PutBucketLoggingPreprocessor : CloudTrailEventPreprocessor {

    override val eventNames = listOf("PutBucketLogging")

    override fun preprocess(event: CloudTrailEvent): CloudTrailEvent {
        return event.copy(
            eventName = "SetBucketLoggingConfiguration",
            request   = event.request?.let { originalRequestMap ->
                originalRequestMap.replaceAndTransform("BucketLoggingStatus", "LoggingConfiguration") {
                    originalRequestMap.mapByPath("BucketLoggingStatus.LoggingEnabled")
                        .rename("TargetPrefix", "LogFilePrefix")
                        .rename("TargetBucket", "DestinationBucketName")
                }
            }
        )
    }

}
