package uy.kohesive.iac.model.aws.cloudtrail

data class CloudTrailEvent(
    val eventId: String,
    val eventSource: String,
    val eventName: String,
    val apiVersion: String?,
    val request: Map<String, Any>?
)
