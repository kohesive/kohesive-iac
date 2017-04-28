package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailEvent
import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

interface RequestPreprocessor : CloudTrailEventPreprocessor {

    fun processRequestMap(requestMap: RequestMap): RequestMap

    override fun preprocess(event: CloudTrailEvent): CloudTrailEvent =
        event.request?.let { requestMap ->
            event.copy(request = processRequestMap(requestMap))
        } ?: event

}