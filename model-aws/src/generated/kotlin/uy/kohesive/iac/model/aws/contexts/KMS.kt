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

}

@DslScope
class KMSContext(context: IacContext) : BaseKMSContext(context) {

}