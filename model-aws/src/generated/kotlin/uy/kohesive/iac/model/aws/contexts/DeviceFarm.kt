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

}

@DslScope
class DeviceFarmContext(context: IacContext) : BaseDeviceFarmContext(context) {

}