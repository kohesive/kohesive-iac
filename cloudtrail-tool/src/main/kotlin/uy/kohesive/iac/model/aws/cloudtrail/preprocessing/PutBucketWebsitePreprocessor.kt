package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailEvent

class PutBucketWebsitePreprocessor : CloudTrailEventPreprocessor {

    override val eventNames = listOf("PutBucketWebsite")

    override fun preprocess(event: CloudTrailEvent): CloudTrailEvent {
        return event.copy(
            eventName = "SetBucketWebsiteConfiguration",
            request   = event.request?.replaceAndTransform("WebsiteConfiguration", "Configuration") {
                it + it.mapByPath("IndexDocument")?.get("Suffix").toMapValue("indexDocumentSuffix") - "IndexDocument"
            }?.minus("website")
        )
    }

}
