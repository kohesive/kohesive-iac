package uy.kohesive.iac.model.aws.cloudtrail

typealias RequestMap = Map<String, Any?>

data class CloudTrailEvent(
    val eventId: String,
    val eventSource: String,
    val eventName: String,
    val apiVersion: String?,
    val request: RequestMap?
)
