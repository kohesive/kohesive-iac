package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.iot.AWSIot
import com.amazonaws.services.iot.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface IotIdentifiable : KohesiveIdentifiable {

}

interface IotEnabled : IotIdentifiable {
    val iotClient: AWSIot
    val iotContext: IotContext
    fun <T> withIotContext(init: IotContext.(AWSIot) -> T): T = iotContext.init(iotClient)
}

open class BaseIotContext(protected val context: IacContext) : IotEnabled by context {

}

@DslScope
class IotContext(context: IacContext) : BaseIotContext(context) {

}