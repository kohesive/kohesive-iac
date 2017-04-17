package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailEvent
import uy.kohesive.iac.model.aws.cloudtrail.RequestMap
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class PutBucketAclPreprocessor : CloudTrailEventPreprocessor {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    override val eventNames = listOf("PutBucketAcl")

    override fun preprocess(event: CloudTrailEvent): CloudTrailEvent {
        return event.copy(
            eventName = "SetBucketAcl",
            request   = event.request?.let { originalRequestMap ->
                val acl = ((originalRequestMap["AccessControlPolicy"] as? RequestMap)?.get("AccessControlList") as? RequestMap)

                val fixedAcl = acl?.let {
                    val grant = (it["Grant"] as? List<RequestMap>)?.map {
                        val grantee = (it["Grantee"] as RequestMap) - "xmlns:xsi"
                        it + mapOf("Grantee" to grantee)
                    }
                    it - "Grant" + mapOf(
                        "Grants" to grant
                    )
                }

                originalRequestMap - "AccessControlPolicy" + mapOf(
                    "acl" to fixedAcl
                )
            }
        )
    }

}