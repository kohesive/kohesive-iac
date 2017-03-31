package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.storagegateway.AWSStorageGateway
import com.amazonaws.services.storagegateway.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface StorageGatewayIdentifiable : KohesiveIdentifiable {

}

interface StorageGatewayEnabled : StorageGatewayIdentifiable {
    val storageGatewayClient: AWSStorageGateway
    val storageGatewayContext: StorageGatewayContext
    fun <T> withStorageGatewayContext(init: StorageGatewayContext.(AWSStorageGateway) -> T): T = storageGatewayContext.init(storageGatewayClient)
}

open class BaseStorageGatewayContext(protected val context: IacContext) : StorageGatewayEnabled by context {


}

@DslScope
class StorageGatewayContext(context: IacContext) : BaseStorageGatewayContext(context) {

}
