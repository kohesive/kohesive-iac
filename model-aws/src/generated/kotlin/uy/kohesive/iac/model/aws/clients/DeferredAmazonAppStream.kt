package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.appstream.AbstractAmazonAppStream
import com.amazonaws.services.appstream.AmazonAppStream
import com.amazonaws.services.appstream.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonAppStream(val context: IacContext) : AbstractAmazonAppStream(), AmazonAppStream {

    override fun createFleet(request: CreateFleetRequest): CreateFleetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateFleetResult().withFleet(
                makeProxy<CreateFleetRequest, Fleet>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateFleetRequest::getName to Fleet::getName,
                        CreateFleetRequest::getDisplayName to Fleet::getDisplayName,
                        CreateFleetRequest::getDescription to Fleet::getDescription,
                        CreateFleetRequest::getImageName to Fleet::getImageName,
                        CreateFleetRequest::getInstanceType to Fleet::getInstanceType,
                        CreateFleetRequest::getMaxUserDurationInSeconds to Fleet::getMaxUserDurationInSeconds,
                        CreateFleetRequest::getDisconnectTimeoutInSeconds to Fleet::getDisconnectTimeoutInSeconds,
                        CreateFleetRequest::getVpcConfig to Fleet::getVpcConfig
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createStack(request: CreateStackRequest): CreateStackResult {
        return with (context) {
            request.registerWithAutoName()
            CreateStackResult().withStack(
                makeProxy<CreateStackRequest, Stack>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateStackRequest::getName to Stack::getName,
                        CreateStackRequest::getDescription to Stack::getDescription,
                        CreateStackRequest::getDisplayName to Stack::getDisplayName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonAppStream(context: IacContext) : BaseDeferredAmazonAppStream(context)
