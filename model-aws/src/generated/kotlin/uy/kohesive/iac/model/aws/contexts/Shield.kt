package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.shield.AWSShield
import com.amazonaws.services.shield.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ShieldIdentifiable : KohesiveIdentifiable {

}

interface ShieldEnabled : ShieldIdentifiable {
    val shieldClient: AWSShield
    val shieldContext: ShieldContext
    fun <T> withShieldContext(init: ShieldContext.(AWSShield) -> T): T = shieldContext.init(shieldClient)
}

open class BaseShieldContext(protected val context: IacContext) : ShieldEnabled by context {

    fun createProtection(name: String, init: CreateProtectionRequest.() -> Unit): CreateProtectionResult {
        return shieldClient.createProtection(CreateProtectionRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class ShieldContext(context: IacContext) : BaseShieldContext(context) {

}
