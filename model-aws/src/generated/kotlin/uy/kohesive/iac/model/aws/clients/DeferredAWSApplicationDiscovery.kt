package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.applicationdiscovery.AbstractAWSApplicationDiscovery
import com.amazonaws.services.applicationdiscovery.AWSApplicationDiscovery
import com.amazonaws.services.applicationdiscovery.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSApplicationDiscovery(val context: IacContext) : AbstractAWSApplicationDiscovery(), AWSApplicationDiscovery {

    override fun createApplication(request: CreateApplicationRequest): CreateApplicationResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateApplicationRequest, CreateApplicationResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createTags(request: CreateTagsRequest): CreateTagsResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTagsResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSApplicationDiscovery(context: IacContext) : BaseDeferredAWSApplicationDiscovery(context)
