package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.pinpoint.AbstractAmazonPinpoint
import com.amazonaws.services.pinpoint.AmazonPinpoint
import com.amazonaws.services.pinpoint.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonPinpoint(val context: IacContext) : AbstractAmazonPinpoint(), AmazonPinpoint {

    override fun createCampaign(request: CreateCampaignRequest): CreateCampaignResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateCampaignRequest, CreateCampaignResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createImportJob(request: CreateImportJobRequest): CreateImportJobResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateImportJobRequest, CreateImportJobResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createSegment(request: CreateSegmentRequest): CreateSegmentResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateSegmentRequest, CreateSegmentResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonPinpoint(context: IacContext) : BaseDeferredAmazonPinpoint(context)
