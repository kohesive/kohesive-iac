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

    fun createPolicy(policyName: String, init: CreatePolicyRequest.() -> Unit): CreatePolicyResult {
        return iotClient.createPolicy(CreatePolicyRequest().apply {
            withPolicyName(policyName)
            init()
        })
    }

    fun createThing(thingName: String, init: CreateThingRequest.() -> Unit): CreateThingResult {
        return iotClient.createThing(CreateThingRequest().apply {
            withThingName(thingName)
            init()
        })
    }

    fun createThingType(thingTypeName: String, init: CreateThingTypeRequest.() -> Unit): CreateThingTypeResult {
        return iotClient.createThingType(CreateThingTypeRequest().apply {
            withThingTypeName(thingTypeName)
            init()
        })
    }


}

@DslScope
class IotContext(context: IacContext) : BaseIotContext(context) {

}
