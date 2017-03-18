package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.iotdata.AWSIotData
import com.amazonaws.services.iotdata.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface IotDataIdentifiable : KohesiveIdentifiable {

}

interface IotDataEnabled : IotDataIdentifiable {
    val iotDataClient: AWSIotData
    val iotDataContext: IotDataContext
    fun <T> withIotDataContext(init: IotDataContext.(AWSIotData) -> T): T = iotDataContext.init(iotDataClient)
}

open class BaseIotDataContext(protected val context: IacContext) : IotDataEnabled by context {

}

@DslScope
class IotDataContext(context: IacContext) : BaseIotDataContext(context) {

}