package uy.kohesive.iac.model.aws.cloudtrail

import com.fasterxml.jackson.databind.node.ObjectNode

data class CloudTrailEvent(
    val eventId: String,
    val eventSource: String,
    val eventName: String,
    val apiVersion: String?,
    val request: ObjectNode?
)
