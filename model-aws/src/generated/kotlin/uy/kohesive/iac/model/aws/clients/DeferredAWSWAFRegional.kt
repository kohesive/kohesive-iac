package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.waf.AbstractAWSWAFRegional
import com.amazonaws.services.waf.AWSWAFRegional
import com.amazonaws.services.waf.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSWAFRegional(val context: IacContext) : AbstractAWSWAFRegional(), AWSWAFRegional {

    override fun createByteMatchSet(request: CreateByteMatchSetRequest): CreateByteMatchSetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateByteMatchSetResult().withByteMatchSet(
                makeProxy<CreateByteMatchSetRequest, ByteMatchSet>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateByteMatchSetRequest::getName to ByteMatchSet::getName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createIPSet(request: CreateIPSetRequest): CreateIPSetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateIPSetResult().withIPSet(
                makeProxy<CreateIPSetRequest, IPSet>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateIPSetRequest::getName to IPSet::getName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createRule(request: CreateRuleRequest): CreateRuleResult {
        return with (context) {
            request.registerWithAutoName()
            CreateRuleResult().withRule(
                makeProxy<CreateRuleRequest, Rule>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateRuleRequest::getName to Rule::getName,
                        CreateRuleRequest::getMetricName to Rule::getMetricName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createSizeConstraintSet(request: CreateSizeConstraintSetRequest): CreateSizeConstraintSetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateSizeConstraintSetResult().withSizeConstraintSet(
                makeProxy<CreateSizeConstraintSetRequest, SizeConstraintSet>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateSizeConstraintSetRequest::getName to SizeConstraintSet::getName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createSqlInjectionMatchSet(request: CreateSqlInjectionMatchSetRequest): CreateSqlInjectionMatchSetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateSqlInjectionMatchSetResult().withSqlInjectionMatchSet(
                makeProxy<CreateSqlInjectionMatchSetRequest, SqlInjectionMatchSet>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateSqlInjectionMatchSetRequest::getName to SqlInjectionMatchSet::getName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createWebACL(request: CreateWebACLRequest): CreateWebACLResult {
        return with (context) {
            request.registerWithAutoName()
            CreateWebACLResult().withWebACL(
                makeProxy<CreateWebACLRequest, WebACL>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateWebACLRequest::getName to WebACL::getName,
                        CreateWebACLRequest::getMetricName to WebACL::getMetricName,
                        CreateWebACLRequest::getDefaultAction to WebACL::getDefaultAction
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createXssMatchSet(request: CreateXssMatchSetRequest): CreateXssMatchSetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateXssMatchSetResult().withXssMatchSet(
                makeProxy<CreateXssMatchSetRequest, XssMatchSet>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateXssMatchSetRequest::getName to XssMatchSet::getName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSWAFRegional(context: IacContext) : BaseDeferredAWSWAFRegional(context)
