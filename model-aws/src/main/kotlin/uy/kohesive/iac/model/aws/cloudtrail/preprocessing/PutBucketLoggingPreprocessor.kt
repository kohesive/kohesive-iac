package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailEvent

class PutBucketLoggingPreprocessor : CloudTrailEventPreprocessor {

    override val eventNames = listOf("PutBucketLogging")

    override fun preprocess(event: CloudTrailEvent): CloudTrailEvent {
        return event.copy(
            eventName = "SetBucketLoggingConfiguration",
            request   = event.request?.let { originalRequestMap ->
                val loggingConfiguration = ((originalRequestMap["BucketLoggingStatus"] as? Map<String, Any>)?.get("LoggingEnabled") as? Map<String, Any>)?.let {
                    val prefix = it["TargetPrefix"]
                    val bucket = it["TargetBucket"]

                    it - "TargetPrefix" - "TargetBucket" + prefix.toMapValue("logFilePrefix") + bucket.toMapValue("destinationBucketName")
                }

                originalRequestMap - "BucketLoggingStatus" + if (loggingConfiguration == null) {
                    emptyMap()
                } else {
                    mapOf("LoggingConfiguration" to loggingConfiguration)
                }
            }
        )
    }

}

fun Any?.toMapValue(name: String): Map<String, Any> {
    if (this == null) {
        return emptyMap()
    } else {
        return mapOf(name to this)
    }
}