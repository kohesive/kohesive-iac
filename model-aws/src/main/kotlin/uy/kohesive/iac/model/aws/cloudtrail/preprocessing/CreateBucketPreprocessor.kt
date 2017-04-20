package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

class CreateBucketPreprocessor : RequestPreprocessor {

    override val eventNames = listOf("CreateBucket")

    override fun processRequestMap(requestMap: RequestMap): RequestMap {
        val configuration = (requestMap["CreateBucketConfiguration"] as? Map<String, Any?>).orEmpty()
        val locationConstraint = configuration["LocationConstraint"] as? String

        // Custom ACL
        val fixedAcl = ((requestMap["AccessControlPolicy"] as? RequestMap)?.get("AccessControlList") as? RequestMap)?.let {
            val fixedGrants = (it["Grant"] as? List<RequestMap>)?.map { grant ->
                val fixedGrantee = (grant["Grantee"] as? RequestMap)?.let { grantee ->
                    val fixedXsiType = (grantee["xsi:type"] as? String)?.let {
                       PutBucketAclPreprocessor.XSITypeFix[it]
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

        // Canned ACL
        val aclFixedRequestMap = requestMap.rename("x-amz-acl", "CannedAcl")!! - "AccessControlPolicy" + mapOf(
            "acl" to fixedAcl
        )

        return (aclFixedRequestMap - "CreateBucketConfiguration") + (locationConstraint?.let {
            mapOf("region" to it)
        } ?: emptyMap())
    }

}