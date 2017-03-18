package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.polly.AmazonPolly
import com.amazonaws.services.polly.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface PollyIdentifiable : KohesiveIdentifiable {

}

interface PollyEnabled : PollyIdentifiable {
    val pollyClient: AmazonPolly
    val pollyContext: PollyContext
    fun <T> withPollyContext(init: PollyContext.(AmazonPolly) -> T): T = pollyContext.init(pollyClient)
}

open class BasePollyContext(protected val context: IacContext) : PollyEnabled by context {

}

@DslScope
class PollyContext(context: IacContext) : BasePollyContext(context) {

}