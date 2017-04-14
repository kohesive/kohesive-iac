package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

class CreateBucketPreprocessor : RequestPreprocessor {

    override val eventNames = listOf("CreateBucket")

    override fun processRequestMap(requestMap: RequestMap): RequestMap {
        val configuration = (requestMap["CreateBucketConfiguration"] as? Map<String, Any?>).orEmpty()
        val locationConstraint = configuration["LocationConstraint"] as? String

        return (requestMap - "CreateBucketConfiguration") + (locationConstraint?.let {
            mapOf("region" to it)
        } ?: emptyMap())
    }

}