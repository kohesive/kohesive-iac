package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.devicefarm.AWSDeviceFarm
import com.amazonaws.services.devicefarm.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface DeviceFarmIdentifiable : KohesiveIdentifiable {

}

interface DeviceFarmEnabled : DeviceFarmIdentifiable {
    val deviceFarmClient: AWSDeviceFarm
    val deviceFarmContext: DeviceFarmContext
    fun <T> withDeviceFarmContext(init: DeviceFarmContext.(AWSDeviceFarm) -> T): T = deviceFarmContext.init(deviceFarmClient)
}

open class BaseDeviceFarmContext(protected val context: IacContext) : DeviceFarmEnabled by context {

    open fun createDevicePool(name: String, init: CreateDevicePoolRequest.() -> Unit): DevicePool {
        return deviceFarmClient.createDevicePool(CreateDevicePoolRequest().apply {
            withName(name)
            init()
        }).getDevicePool()
    }

    open fun createNetworkProfile(name: String, init: CreateNetworkProfileRequest.() -> Unit): NetworkProfile {
        return deviceFarmClient.createNetworkProfile(CreateNetworkProfileRequest().apply {
            withName(name)
            init()
        }).getNetworkProfile()
    }

    open fun createProject(name: String, init: CreateProjectRequest.() -> Unit): Project {
        return deviceFarmClient.createProject(CreateProjectRequest().apply {
            withName(name)
            init()
        }).getProject()
    }

    open fun createRemoteAccessSession(name: String, init: CreateRemoteAccessSessionRequest.() -> Unit): RemoteAccessSession {
        return deviceFarmClient.createRemoteAccessSession(CreateRemoteAccessSessionRequest().apply {
            withName(name)
            init()
        }).getRemoteAccessSession()
    }

    open fun createUpload(name: String, init: CreateUploadRequest.() -> Unit): Upload {
        return deviceFarmClient.createUpload(CreateUploadRequest().apply {
            withName(name)
            init()
        }).getUpload()
    }


}

@DslScope
class DeviceFarmContext(context: IacContext) : BaseDeviceFarmContext(context) {

}
