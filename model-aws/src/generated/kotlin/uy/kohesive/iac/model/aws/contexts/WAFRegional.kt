package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.waf.AWSWAFRegional
import com.amazonaws.services.waf.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface WAFRegionalIdentifiable : KohesiveIdentifiable {

}

interface WAFRegionalEnabled : WAFRegionalIdentifiable {
    val wAFRegionalClient: AWSWAFRegional
    val wAFRegionalContext: WAFRegionalContext
    fun <T> withWAFRegionalContext(init: WAFRegionalContext.(AWSWAFRegional) -> T): T = wAFRegionalContext.init(wAFRegionalClient)
}

open class BaseWAFRegionalContext(protected val context: IacContext) : WAFRegionalEnabled by context {

}

@DslScope
class WAFRegionalContext(context: IacContext) : BaseWAFRegionalContext(context) {

}