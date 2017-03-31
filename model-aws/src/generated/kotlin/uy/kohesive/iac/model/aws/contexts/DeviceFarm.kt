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

    fun createDevicePool(name: String, init: CreateDevicePoolRequest.() -> Unit): DevicePool {
        return deviceFarmClient.createDevicePool(CreateDevicePoolRequest().apply {
            withName(name)
            init()
        }).devicePool
    }

    fun createNetworkProfile(name: String, init: CreateNetworkProfileRequest.() -> Unit): NetworkProfile {
        return deviceFarmClient.createNetworkProfile(CreateNetworkProfileRequest().apply {
            withName(name)
            init()
        }).networkProfile
    }

    fun createProject(name: String, init: CreateProjectRequest.() -> Unit): Project {
        return deviceFarmClient.createProject(CreateProjectRequest().apply {
            withName(name)
            init()
        }).project
    }

    fun createRemoteAccessSession(name: String, init: CreateRemoteAccessSessionRequest.() -> Unit): RemoteAccessSession {
        return deviceFarmClient.createRemoteAccessSession(CreateRemoteAccessSessionRequest().apply {
            withName(name)
            init()
        }).remoteAccessSession
    }

    fun createUpload(name: String, init: CreateUploadRequest.() -> Unit): Upload {
        return deviceFarmClient.createUpload(CreateUploadRequest().apply {
            withName(name)
            init()
        }).upload
    }


}

@DslScope
class DeviceFarmContext(context: IacContext) : BaseDeviceFarmContext(context) {

}
