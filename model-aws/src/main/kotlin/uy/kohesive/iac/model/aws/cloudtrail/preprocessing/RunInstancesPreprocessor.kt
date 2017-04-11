package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

class RunInstancesPreprocessor : RequestPreprocessor {

    override val eventNames = listOf("RunInstances")

    override fun process(requestMap: RequestMap): RequestMap {
        // Fix instances parameters
        val instancesSet  = requestMap["instancesSet"] as? RequestMap
        val instanceProps = (instancesSet?.get("items") as? List<RequestMap>)?.firstOrNull().orEmpty()

        // Fix group IDs/names
        val groupSet   = (requestMap["groupSet"] as? RequestMap).orEmpty()
        val groupItems = groupSet["items"] as? List<RequestMap>
        val groupIds   = groupItems?.map { it["groupId"] }
        val groupNames = groupItems?.map { it["groupName"] }

        // Remove 'availabilityZone' parameter
        return (requestMap - "instancesSet" - "availabilityZone" - "groupSet") + instanceProps + mapOf(
            "securityGroups"   to groupNames,
            "securityGroupIds" to groupIds
        )
    }

}