package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailEvent

class PutBucketTaggingPreprocessor : CloudTrailEventPreprocessor {

    override val eventNames = listOf("PutBucketTagging")

    override fun preprocess(event: CloudTrailEvent): CloudTrailEvent {
        return event.copy(
            eventName = "SetBucketTaggingConfiguration",
            request   = event.request?.let { request ->
                request.rename("Tagging", "TaggingConfiguration")!! - "tagging"
            }
        )
    }

}