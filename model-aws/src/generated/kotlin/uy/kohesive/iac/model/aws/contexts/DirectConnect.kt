package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.directconnect.AmazonDirectConnect
import com.amazonaws.services.directconnect.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface DirectConnectIdentifiable : KohesiveIdentifiable {

}

interface DirectConnectEnabled : DirectConnectIdentifiable {
    val directConnectClient: AmazonDirectConnect
    val directConnectContext: DirectConnectContext
    fun <T> withDirectConnectContext(init: DirectConnectContext.(AmazonDirectConnect) -> T): T = directConnectContext.init(directConnectClient)
}

open class BaseDirectConnectContext(protected val context: IacContext) : DirectConnectEnabled by context {

    open fun createConnection(connectionName: String, init: CreateConnectionRequest.() -> Unit): CreateConnectionResult {
        return directConnectClient.createConnection(CreateConnectionRequest().apply {
            withConnectionName(connectionName)
            init()
        })
    }

    open fun createInterconnect(interconnectName: String, init: CreateInterconnectRequest.() -> Unit): CreateInterconnectResult {
        return directConnectClient.createInterconnect(CreateInterconnectRequest().apply {
            withInterconnectName(interconnectName)
            init()
        })
    }

    open fun createLag(lagName: String, init: CreateLagRequest.() -> Unit): CreateLagResult {
        return directConnectClient.createLag(CreateLagRequest().apply {
            withLagName(lagName)
            init()
        })
    }


}

@DslScope
class DirectConnectContext(context: IacContext) : BaseDirectConnectContext(context) {

}
