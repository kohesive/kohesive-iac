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

    open fun createByteMatchSet(name: String, init: CreateByteMatchSetRequest.() -> Unit): ByteMatchSet {
        return wAFRegionalClient.createByteMatchSet(CreateByteMatchSetRequest().apply {
            withName(name)
            init()
        }).getByteMatchSet()
    }

    open fun createIPSet(name: String, init: CreateIPSetRequest.() -> Unit): IPSet {
        return wAFRegionalClient.createIPSet(CreateIPSetRequest().apply {
            withName(name)
            init()
        }).getIPSet()
    }

    open fun createRule(name: String, init: CreateRuleRequest.() -> Unit): Rule {
        return wAFRegionalClient.createRule(CreateRuleRequest().apply {
            withName(name)
            init()
        }).getRule()
    }

    open fun createSizeConstraintSet(name: String, init: CreateSizeConstraintSetRequest.() -> Unit): SizeConstraintSet {
        return wAFRegionalClient.createSizeConstraintSet(CreateSizeConstraintSetRequest().apply {
            withName(name)
            init()
        }).getSizeConstraintSet()
    }

    open fun createSqlInjectionMatchSet(name: String, init: CreateSqlInjectionMatchSetRequest.() -> Unit): SqlInjectionMatchSet {
        return wAFRegionalClient.createSqlInjectionMatchSet(CreateSqlInjectionMatchSetRequest().apply {
            withName(name)
            init()
        }).getSqlInjectionMatchSet()
    }

    open fun createWebACL(name: String, init: CreateWebACLRequest.() -> Unit): WebACL {
        return wAFRegionalClient.createWebACL(CreateWebACLRequest().apply {
            withName(name)
            init()
        }).getWebACL()
    }

    open fun createXssMatchSet(name: String, init: CreateXssMatchSetRequest.() -> Unit): XssMatchSet {
        return wAFRegionalClient.createXssMatchSet(CreateXssMatchSetRequest().apply {
            withName(name)
            init()
        }).getXssMatchSet()
    }


}

@DslScope
class WAFRegionalContext(context: IacContext) : BaseWAFRegionalContext(context) {

}
