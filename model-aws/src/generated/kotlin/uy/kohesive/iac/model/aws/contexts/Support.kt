package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.support.AWSSupport
import com.amazonaws.services.support.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface SupportIdentifiable : KohesiveIdentifiable {

}

interface SupportEnabled : SupportIdentifiable {
    val supportClient: AWSSupport
    val supportContext: SupportContext
    fun <T> withSupportContext(init: SupportContext.(AWSSupport) -> T): T = supportContext.init(supportClient)
}

open class BaseSupportContext(protected val context: IacContext) : SupportEnabled by context {


}

@DslScope
class SupportContext(context: IacContext) : BaseSupportContext(context) {

}
