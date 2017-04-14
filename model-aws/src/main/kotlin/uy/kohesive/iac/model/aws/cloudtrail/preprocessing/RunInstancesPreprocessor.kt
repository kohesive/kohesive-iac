package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

class RunInstancesPreprocessor : RequestPreprocessor {

    override val eventNames = listOf("RunInstances")

    override fun processRequestMap(requestMap: RequestMap): RequestMap {
        // Fix instances parameters
        val instancesSet  = requestMap["instancesSet"] as? RequestMap
        val instanceProps = (instancesSet?.get("items") as? List<RequestMap>)?.firstOrNull().orEmpty()

        // Fix group IDs/names
        val groupSet   = (requestMap["groupSet"] as? RequestMap).orEmpty()
        val groupItems = groupSet["items"] as? List<RequestMap>
        val groupIds   = groupItems?.map { it["groupId"] }
        val groupNames = groupItems?.map { it["groupName"] }

        // Fix 'tenancy', put it in placement
        val oldPlacement = (requestMap["placement"] as? Map<String, Any?>).orEmpty()
        val newPlacement = (requestMap["tenancy"] as? String)?.let { tenancy ->
            oldPlacement + mapOf(
                "tenancy" to tenancy
            )
        } ?: oldPlacement
        val placementPairMap = if (newPlacement.isNotEmpty()) {
            mapOf("placement" to newPlacement)
        } else {
            emptyMap()
        }

        return (requestMap - "instancesSet" - "availabilityZone" - "groupSet" - "tenancy") + instanceProps + mapOf(
            "securityGroups"   to groupNames,
            "securityGroupIds" to groupIds
        ) + placementPairMap
    }

}