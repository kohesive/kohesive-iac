package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.glacier.AmazonGlacier
import com.amazonaws.services.glacier.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface GlacierIdentifiable : KohesiveIdentifiable {

}

interface GlacierEnabled : GlacierIdentifiable {
    val glacierClient: AmazonGlacier
    val glacierContext: GlacierContext
    fun <T> withGlacierContext(init: GlacierContext.(AmazonGlacier) -> T): T = glacierContext.init(glacierClient)
}

open class BaseGlacierContext(protected val context: IacContext) : GlacierEnabled by context {

}

@DslScope
class GlacierContext(context: IacContext) : BaseGlacierContext(context) {

}