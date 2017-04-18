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

        val XSITypeFix = mapOf(
            "CanonicalUser" to "CanonicalGrantee",
            "Group"         to "GroupGrantee"
        )
    }

    override val eventNames = listOf("PutBucketAcl")

    override fun preprocess(event: CloudTrailEvent): CloudTrailEvent {
        return event.copy(
            eventName = "SetBucketAcl",
            request   = event.request?.let { originalRequestMap ->
                val fixedAcl = ((originalRequestMap["AccessControlPolicy"] as? RequestMap)?.get("AccessControlList") as? RequestMap)?.let {
                    val fixedGrants = (it["Grant"] as? List<RequestMap>)?.map { grant ->
                        val fixedGrantee = (grant["Grantee"] as? RequestMap)?.let { grantee ->
                            val fixedXsiType = (grantee["xsi:type"] as? String)?.let {
                               XSITypeFix[it]
                            }

                            val id  = grantee["ID"] as? String?
                            val uri = grantee["URI"] as? String?

                            grantee - "ID" - "URI" +
                                (if (fixedXsiType != null) mapOf("xsi:type" to fixedXsiType) else emptyMap()) +
                                (if (id != null) mapOf("identifier" to id) else emptyMap()) +
                                (if (uri != null) mapOf("identifier" to uri) else emptyMap())
                        }

                        grant + mapOf(
                            "Grantee" to fixedGrantee
                        )
                    }

                    it - "Grant" + mapOf(
                        "Grants" to fixedGrants
                    )
                }

                originalRequestMap - "AccessControlPolicy" + mapOf(
                    "acl" to fixedAcl
                )
            }
        )
    }

}