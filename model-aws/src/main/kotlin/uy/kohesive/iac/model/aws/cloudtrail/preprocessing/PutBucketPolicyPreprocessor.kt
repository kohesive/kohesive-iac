package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailEvent
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

/**
 * This pre-processor fixes three things:
 *
 * 1) Event name changed to SetBucketPolicy to comply with AWS S3 API
 * 2) Misspelled 'buckeyPolicy' is changed to 'bucketPolicy'
 * 3) 'bucketPolicy' tree is converted to JSON string and put into 'policyText'
 *
 */
class PutBucketPolicyPreprocessor : CloudTrailEventPreprocessor {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    override val eventNames = listOf("PutBucketPolicy")

    override fun preprocess(event: CloudTrailEvent): CloudTrailEvent {
        return event.copy(
            eventName = "SetBucketPolicy",
            request   = event.request?.let { requestMap ->
                // Bucket policy is spelled 'buc'
                val bucketPolicyAsJSON = ((requestMap["buckeyPolicy"] ?: requestMap["bucketPolicy"]) as? Map<String, Any?>)?.let { policyMap ->
                    JSON.writeValueAsString(policyMap)
                }

                (requestMap - "buckeyPolicy" - "bucketPolicy") + bucketPolicyAsJSON?.let { json ->
                    mapOf("policyText" to json)
                }.orEmpty()
            }
        )
    }

}