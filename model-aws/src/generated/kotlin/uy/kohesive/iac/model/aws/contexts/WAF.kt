package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.waf.AWSWAF
import com.amazonaws.services.waf.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface WAFIdentifiable : KohesiveIdentifiable {

}

interface WAFEnabled : WAFIdentifiable {
    val wafClient: AWSWAF
    val wafContext: WAFContext
    fun <T> withWAFContext(init: WAFContext.(AWSWAF) -> T): T = wafContext.init(wafClient)
}

open class BaseWAFContext(protected val context: IacContext) : WAFEnabled by context {

}

@DslScope
class WAFContext(context: IacContext) : BaseWAFContext(context) {

}