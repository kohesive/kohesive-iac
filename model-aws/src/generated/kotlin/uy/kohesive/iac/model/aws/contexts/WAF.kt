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

    open fun createByteMatchSet(name: String, init: CreateByteMatchSetRequest.() -> Unit): ByteMatchSet {
        return wafClient.createByteMatchSet(CreateByteMatchSetRequest().apply {
            withName(name)
            init()
        }).getByteMatchSet()
    }

    open fun createIPSet(name: String, init: CreateIPSetRequest.() -> Unit): IPSet {
        return wafClient.createIPSet(CreateIPSetRequest().apply {
            withName(name)
            init()
        }).getIPSet()
    }

    open fun createRule(name: String, init: CreateRuleRequest.() -> Unit): Rule {
        return wafClient.createRule(CreateRuleRequest().apply {
            withName(name)
            init()
        }).getRule()
    }

    open fun createSizeConstraintSet(name: String, init: CreateSizeConstraintSetRequest.() -> Unit): SizeConstraintSet {
        return wafClient.createSizeConstraintSet(CreateSizeConstraintSetRequest().apply {
            withName(name)
            init()
        }).getSizeConstraintSet()
    }

    open fun createSqlInjectionMatchSet(name: String, init: CreateSqlInjectionMatchSetRequest.() -> Unit): SqlInjectionMatchSet {
        return wafClient.createSqlInjectionMatchSet(CreateSqlInjectionMatchSetRequest().apply {
            withName(name)
            init()
        }).getSqlInjectionMatchSet()
    }

    open fun createWebACL(name: String, init: CreateWebACLRequest.() -> Unit): WebACL {
        return wafClient.createWebACL(CreateWebACLRequest().apply {
            withName(name)
            init()
        }).getWebACL()
    }

    open fun createXssMatchSet(name: String, init: CreateXssMatchSetRequest.() -> Unit): XssMatchSet {
        return wafClient.createXssMatchSet(CreateXssMatchSetRequest().apply {
            withName(name)
            init()
        }).getXssMatchSet()
    }


}

@DslScope
class WAFContext(context: IacContext) : BaseWAFContext(context) {

}
