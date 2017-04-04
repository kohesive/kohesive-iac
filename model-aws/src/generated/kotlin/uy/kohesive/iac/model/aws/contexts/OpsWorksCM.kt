package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.opsworkscm.AWSOpsWorksCM
import com.amazonaws.services.opsworkscm.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface OpsWorksCMIdentifiable : KohesiveIdentifiable {

}

interface OpsWorksCMEnabled : OpsWorksCMIdentifiable {
    val opsWorksCMClient: AWSOpsWorksCM
    val opsWorksCMContext: OpsWorksCMContext
    fun <T> withOpsWorksCMContext(init: OpsWorksCMContext.(AWSOpsWorksCM) -> T): T = opsWorksCMContext.init(opsWorksCMClient)
}

open class BaseOpsWorksCMContext(protected val context: IacContext) : OpsWorksCMEnabled by context {

    open fun createServer(serverName: String, init: CreateServerRequest.() -> Unit): Server {
        return opsWorksCMClient.createServer(CreateServerRequest().apply {
            withServerName(serverName)
            init()
        }).getServer()
    }


}

@DslScope
class OpsWorksCMContext(context: IacContext) : BaseOpsWorksCMContext(context) {

}
