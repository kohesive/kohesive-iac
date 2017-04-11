package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

class CreateNetworkInterfacePreprocessor : RequestPreprocessor {

    override val eventNames = listOf("CreateNetworkInterface")

    override fun process(requestMap: RequestMap): RequestMap {
        // Fix group IDs/names
        val groupSet   = (requestMap["groupSet"] as? RequestMap).orEmpty()
        val groupItems = groupSet["items"] as? List<RequestMap>
        val groupIds   = groupItems?.map { it["groupId"] }

        return (requestMap - "groupSet") + mapOf(
            "groups" to groupIds
        )
    }

}