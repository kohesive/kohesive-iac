package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

class AttachVolumePreprocessor : RequestPreprocessor {

    override val eventNames = listOf("AttachVolume")

    override fun processRequestMap(requestMap: RequestMap): RequestMap {
        return requestMap - "deleteOnTermination"
    }
}