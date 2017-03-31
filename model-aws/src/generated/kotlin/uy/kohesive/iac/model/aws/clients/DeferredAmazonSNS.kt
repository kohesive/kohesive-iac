package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.sns.AbstractAmazonSNS
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonSNS(val context: IacContext) : AbstractAmazonSNS(), AmazonSNS {

    override fun addPermission(request: AddPermissionRequest): AddPermissionResult {
        return with (context) {
            request.registerWithAutoName()
            AddPermissionResult().registerWithSameNameAs(request)
        }
    }

    override fun createPlatformApplication(request: CreatePlatformApplicationRequest): CreatePlatformApplicationResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePlatformApplicationRequest, CreatePlatformApplicationResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createPlatformEndpoint(request: CreatePlatformEndpointRequest): CreatePlatformEndpointResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePlatformEndpointRequest, CreatePlatformEndpointResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createTopic(request: CreateTopicRequest): CreateTopicResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateTopicRequest, CreateTopicResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonSNS(context: IacContext) : BaseDeferredAmazonSNS(context)
