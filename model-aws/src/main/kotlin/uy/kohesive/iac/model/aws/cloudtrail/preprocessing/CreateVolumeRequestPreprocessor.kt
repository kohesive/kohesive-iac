package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

class CreateVolumeRequestPreprocessor : RequestPreprocessor {

    override val eventNames = listOf("CreateVolume")

    override fun processRequestMap(requestMap: RequestMap): RequestMap {
        return requestMap.rename("zone", "availabilityZone")!!
    }

}
