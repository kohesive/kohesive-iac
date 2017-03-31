package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.directconnect.AbstractAmazonDirectConnect
import com.amazonaws.services.directconnect.AmazonDirectConnect
import com.amazonaws.services.directconnect.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonDirectConnect(val context: IacContext) : AbstractAmazonDirectConnect(), AmazonDirectConnect {

    override fun createBGPPeer(request: CreateBGPPeerRequest): CreateBGPPeerResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateBGPPeerRequest, CreateBGPPeerResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createConnection(request: CreateConnectionRequest): CreateConnectionResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateConnectionRequest, CreateConnectionResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateConnectionRequest::getConnectionName to CreateConnectionResult::getConnectionName,
                    CreateConnectionRequest::getLocation to CreateConnectionResult::getLocation,
                    CreateConnectionRequest::getBandwidth to CreateConnectionResult::getBandwidth,
                    CreateConnectionRequest::getLagId to CreateConnectionResult::getLagId
                )
            )
        }
    }

    override fun createInterconnect(request: CreateInterconnectRequest): CreateInterconnectResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateInterconnectRequest, CreateInterconnectResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateInterconnectRequest::getInterconnectName to CreateInterconnectResult::getInterconnectName,
                    CreateInterconnectRequest::getLocation to CreateInterconnectResult::getLocation,
                    CreateInterconnectRequest::getBandwidth to CreateInterconnectResult::getBandwidth,
                    CreateInterconnectRequest::getLagId to CreateInterconnectResult::getLagId
                )
            )
        }
    }

    override fun createLag(request: CreateLagRequest): CreateLagResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateLagRequest, CreateLagResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateLagRequest::getConnectionsBandwidth to CreateLagResult::getConnectionsBandwidth,
                    CreateLagRequest::getNumberOfConnections to CreateLagResult::getNumberOfConnections,
                    CreateLagRequest::getLagName to CreateLagResult::getLagName,
                    CreateLagRequest::getLocation to CreateLagResult::getLocation
                )
            )
        }
    }

    override fun createPrivateVirtualInterface(request: CreatePrivateVirtualInterfaceRequest): CreatePrivateVirtualInterfaceResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePrivateVirtualInterfaceRequest, CreatePrivateVirtualInterfaceResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreatePrivateVirtualInterfaceRequest::getConnectionId to CreatePrivateVirtualInterfaceResult::getConnectionId
                )
            )
        }
    }

    override fun createPublicVirtualInterface(request: CreatePublicVirtualInterfaceRequest): CreatePublicVirtualInterfaceResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePublicVirtualInterfaceRequest, CreatePublicVirtualInterfaceResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreatePublicVirtualInterfaceRequest::getConnectionId to CreatePublicVirtualInterfaceResult::getConnectionId
                )
            )
        }
    }


}

class DeferredAmazonDirectConnect(context: IacContext) : BaseDeferredAmazonDirectConnect(context)
