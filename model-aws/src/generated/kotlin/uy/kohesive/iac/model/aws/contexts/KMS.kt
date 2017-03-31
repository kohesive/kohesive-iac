package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface KMSIdentifiable : KohesiveIdentifiable {

}

interface KMSEnabled : KMSIdentifiable {
    val kmsClient: AWSKMS
    val kmsContext: KMSContext
    fun <T> withKMSContext(init: KMSContext.(AWSKMS) -> T): T = kmsContext.init(kmsClient)
}

open class BaseKMSContext(protected val context: IacContext) : KMSEnabled by context {

    fun createAlias(aliasName: String, init: CreateAliasRequest.() -> Unit): CreateAliasResult {
        return kmsClient.createAlias(CreateAliasRequest().apply {
            withAliasName(aliasName)
            init()
        })
    }

    fun createGrant(name: String, init: CreateGrantRequest.() -> Unit): CreateGrantResult {
        return kmsClient.createGrant(CreateGrantRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class KMSContext(context: IacContext) : BaseKMSContext(context) {

}
