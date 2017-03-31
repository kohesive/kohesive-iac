package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.iot.AbstractAWSIot
import com.amazonaws.services.iot.AWSIot
import com.amazonaws.services.iot.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSIot(val context: IacContext) : AbstractAWSIot(), AWSIot {

    override fun attachPrincipalPolicy(request: AttachPrincipalPolicyRequest): AttachPrincipalPolicyResult {
        return with (context) {
            request.registerWithAutoName()
            AttachPrincipalPolicyResult().registerWithSameNameAs(request)
        }
    }

    override fun attachThingPrincipal(request: AttachThingPrincipalRequest): AttachThingPrincipalResult {
        return with (context) {
            request.registerWithAutoName()
            AttachThingPrincipalResult().registerWithSameNameAs(request)
        }
    }

    override fun createCertificateFromCsr(request: CreateCertificateFromCsrRequest): CreateCertificateFromCsrResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateCertificateFromCsrRequest, CreateCertificateFromCsrResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createKeysAndCertificate(request: CreateKeysAndCertificateRequest): CreateKeysAndCertificateResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateKeysAndCertificateRequest, CreateKeysAndCertificateResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createPolicy(request: CreatePolicyRequest): CreatePolicyResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePolicyRequest, CreatePolicyResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreatePolicyRequest::getPolicyName to CreatePolicyResult::getPolicyName,
                    CreatePolicyRequest::getPolicyDocument to CreatePolicyResult::getPolicyDocument
                )
            )
        }
    }

    override fun createPolicyVersion(request: CreatePolicyVersionRequest): CreatePolicyVersionResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePolicyVersionRequest, CreatePolicyVersionResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreatePolicyVersionRequest::getPolicyDocument to CreatePolicyVersionResult::getPolicyDocument
                )
            )
        }
    }

    override fun createThing(request: CreateThingRequest): CreateThingResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateThingRequest, CreateThingResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateThingRequest::getThingName to CreateThingResult::getThingName
                )
            )
        }
    }

    override fun createThingType(request: CreateThingTypeRequest): CreateThingTypeResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateThingTypeRequest, CreateThingTypeResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateThingTypeRequest::getThingTypeName to CreateThingTypeResult::getThingTypeName
                )
            )
        }
    }

    override fun createTopicRule(request: CreateTopicRuleRequest): CreateTopicRuleResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTopicRuleResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSIot(context: IacContext) : BaseDeferredAWSIot(context)
