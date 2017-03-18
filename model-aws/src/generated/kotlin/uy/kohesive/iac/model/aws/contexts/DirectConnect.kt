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

}

@DslScope
class DirectConnectContext(context: IacContext) : BaseDirectConnectContext(context) {

}