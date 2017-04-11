package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

class RunInstancesPreprocessor : RequestPreprocessor {

    override val eventNames = listOf("RunInstances")

    override fun process(requestMap: RequestMap): RequestMap {
        val instancesSet  = requestMap["instancesSet"] as? RequestMap
        val instanceProps = (instancesSet?.get("items") as? List<RequestMap>)?.firstOrNull().orEmpty()
        return (requestMap - "instancesSet") + instanceProps
    }

}