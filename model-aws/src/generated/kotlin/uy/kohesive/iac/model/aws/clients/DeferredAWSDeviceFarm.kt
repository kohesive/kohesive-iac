package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.devicefarm.AbstractAWSDeviceFarm
import com.amazonaws.services.devicefarm.AWSDeviceFarm
import com.amazonaws.services.devicefarm.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSDeviceFarm(val context: IacContext) : AbstractAWSDeviceFarm(), AWSDeviceFarm {

    override fun createDevicePool(request: CreateDevicePoolRequest): CreateDevicePoolResult {
        return with (context) {
            request.registerWithAutoName()
            CreateDevicePoolResult().withDevicePool(
                makeProxy<CreateDevicePoolRequest, DevicePool>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateDevicePoolRequest::getName to DevicePool::getName,
                        CreateDevicePoolRequest::getDescription to DevicePool::getDescription,
                        CreateDevicePoolRequest::getRules to DevicePool::getRules
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createNetworkProfile(request: CreateNetworkProfileRequest): CreateNetworkProfileResult {
        return with (context) {
            request.registerWithAutoName()
            CreateNetworkProfileResult().withNetworkProfile(
                makeProxy<CreateNetworkProfileRequest, NetworkProfile>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateNetworkProfileRequest::getName to NetworkProfile::getName,
                        CreateNetworkProfileRequest::getDescription to NetworkProfile::getDescription,
                        CreateNetworkProfileRequest::getType to NetworkProfile::getType,
                        CreateNetworkProfileRequest::getUplinkBandwidthBits to NetworkProfile::getUplinkBandwidthBits,
                        CreateNetworkProfileRequest::getDownlinkBandwidthBits to NetworkProfile::getDownlinkBandwidthBits,
                        CreateNetworkProfileRequest::getUplinkDelayMs to NetworkProfile::getUplinkDelayMs,
                        CreateNetworkProfileRequest::getDownlinkDelayMs to NetworkProfile::getDownlinkDelayMs,
                        CreateNetworkProfileRequest::getUplinkJitterMs to NetworkProfile::getUplinkJitterMs,
                        CreateNetworkProfileRequest::getDownlinkJitterMs to NetworkProfile::getDownlinkJitterMs,
                        CreateNetworkProfileRequest::getUplinkLossPercent to NetworkProfile::getUplinkLossPercent,
                        CreateNetworkProfileRequest::getDownlinkLossPercent to NetworkProfile::getDownlinkLossPercent
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createProject(request: CreateProjectRequest): CreateProjectResult {
        return with (context) {
            request.registerWithAutoName()
            CreateProjectResult().withProject(
                makeProxy<CreateProjectRequest, Project>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateProjectRequest::getName to Project::getName,
                        CreateProjectRequest::getDefaultJobTimeoutMinutes to Project::getDefaultJobTimeoutMinutes
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createRemoteAccessSession(request: CreateRemoteAccessSessionRequest): CreateRemoteAccessSessionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateRemoteAccessSessionResult().withRemoteAccessSession(
                makeProxy<CreateRemoteAccessSessionRequest, RemoteAccessSession>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateRemoteAccessSessionRequest::getName to RemoteAccessSession::getName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createUpload(request: CreateUploadRequest): CreateUploadResult {
        return with (context) {
            request.registerWithAutoName()
            CreateUploadResult().withUpload(
                makeProxy<CreateUploadRequest, Upload>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateUploadRequest::getName to Upload::getName,
                        CreateUploadRequest::getType to Upload::getType,
                        CreateUploadRequest::getContentType to Upload::getContentType
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSDeviceFarm(context: IacContext) : BaseDeferredAWSDeviceFarm(context)
