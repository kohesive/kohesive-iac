package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

class CreateLoadBalancerPreprocessor : RequestPreprocessor {

    override val eventNames = listOf("CreateLoadBalancer")

    override fun processRequestMap(requestMap: RequestMap): RequestMap {
        val subnets = (requestMap["subnetMappings"] as? List<RequestMap>)?.map {
            it["subnetId"]
        }?.filterNotNull()

        return requestMap - "type" - "subnetMappings" + subnets.toMapValue("subnets")
    }
}