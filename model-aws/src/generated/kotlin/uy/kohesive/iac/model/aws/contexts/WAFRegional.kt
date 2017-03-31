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

    fun createByteMatchSet(name: String, init: CreateByteMatchSetRequest.() -> Unit): ByteMatchSet {
        return wAFRegionalClient.createByteMatchSet(CreateByteMatchSetRequest().apply {
            withName(name)
            init()
        }).byteMatchSet
    }

    fun createIPSet(name: String, init: CreateIPSetRequest.() -> Unit): IPSet {
        return wAFRegionalClient.createIPSet(CreateIPSetRequest().apply {
            withName(name)
            init()
        }).ipSet
    }

    fun createRule(name: String, init: CreateRuleRequest.() -> Unit): Rule {
        return wAFRegionalClient.createRule(CreateRuleRequest().apply {
            withName(name)
            init()
        }).rule
    }

    fun createSizeConstraintSet(name: String, init: CreateSizeConstraintSetRequest.() -> Unit): SizeConstraintSet {
        return wAFRegionalClient.createSizeConstraintSet(CreateSizeConstraintSetRequest().apply {
            withName(name)
            init()
        }).sizeConstraintSet
    }

    fun createSqlInjectionMatchSet(name: String, init: CreateSqlInjectionMatchSetRequest.() -> Unit): SqlInjectionMatchSet {
        return wAFRegionalClient.createSqlInjectionMatchSet(CreateSqlInjectionMatchSetRequest().apply {
            withName(name)
            init()
        }).sqlInjectionMatchSet
    }

    fun createWebACL(name: String, init: CreateWebACLRequest.() -> Unit): WebACL {
        return wAFRegionalClient.createWebACL(CreateWebACLRequest().apply {
            withName(name)
            init()
        }).webACL
    }

    fun createXssMatchSet(name: String, init: CreateXssMatchSetRequest.() -> Unit): XssMatchSet {
        return wAFRegionalClient.createXssMatchSet(CreateXssMatchSetRequest().apply {
            withName(name)
            init()
        }).xssMatchSet
    }


}

@DslScope
class WAFRegionalContext(context: IacContext) : BaseWAFRegionalContext(context) {

}
