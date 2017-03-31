package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.mturk.AbstractAmazonMTurk
import com.amazonaws.services.mturk.AmazonMTurk
import com.amazonaws.services.mturk.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonMTurk(val context: IacContext) : AbstractAmazonMTurk(), AmazonMTurk {

    override fun createHIT(request: CreateHITRequest): CreateHITResult {
        return with (context) {
            request.registerWithAutoName()
            CreateHITResult().withHIT(
                makeProxy<CreateHITRequest, HIT>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateHITRequest::getHITLayoutId to HIT::getHITLayoutId,
                        CreateHITRequest::getTitle to HIT::getTitle,
                        CreateHITRequest::getDescription to HIT::getDescription,
                        CreateHITRequest::getQuestion to HIT::getQuestion,
                        CreateHITRequest::getKeywords to HIT::getKeywords,
                        CreateHITRequest::getMaxAssignments to HIT::getMaxAssignments,
                        CreateHITRequest::getReward to HIT::getReward,
                        CreateHITRequest::getAutoApprovalDelayInSeconds to HIT::getAutoApprovalDelayInSeconds,
                        CreateHITRequest::getAssignmentDurationInSeconds to HIT::getAssignmentDurationInSeconds,
                        CreateHITRequest::getRequesterAnnotation to HIT::getRequesterAnnotation,
                        CreateHITRequest::getQualificationRequirements to HIT::getQualificationRequirements
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createHITWithHITType(request: CreateHITWithHITTypeRequest): CreateHITWithHITTypeResult {
        return with (context) {
            request.registerWithAutoName()
            CreateHITWithHITTypeResult().withHIT(
                makeProxy<CreateHITWithHITTypeRequest, HIT>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateHITWithHITTypeRequest::getHITTypeId to HIT::getHITTypeId,
                        CreateHITWithHITTypeRequest::getHITLayoutId to HIT::getHITLayoutId,
                        CreateHITWithHITTypeRequest::getQuestion to HIT::getQuestion,
                        CreateHITWithHITTypeRequest::getMaxAssignments to HIT::getMaxAssignments,
                        CreateHITWithHITTypeRequest::getRequesterAnnotation to HIT::getRequesterAnnotation
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createQualificationType(request: CreateQualificationTypeRequest): CreateQualificationTypeResult {
        return with (context) {
            request.registerWithAutoName()
            CreateQualificationTypeResult().withQualificationType(
                makeProxy<CreateQualificationTypeRequest, QualificationType>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateQualificationTypeRequest::getName to QualificationType::getName,
                        CreateQualificationTypeRequest::getDescription to QualificationType::getDescription,
                        CreateQualificationTypeRequest::getKeywords to QualificationType::getKeywords,
                        CreateQualificationTypeRequest::getQualificationTypeStatus to QualificationType::getQualificationTypeStatus,
                        CreateQualificationTypeRequest::getTest to QualificationType::getTest,
                        CreateQualificationTypeRequest::getTestDurationInSeconds to QualificationType::getTestDurationInSeconds,
                        CreateQualificationTypeRequest::getAnswerKey to QualificationType::getAnswerKey,
                        CreateQualificationTypeRequest::getRetryDelayInSeconds to QualificationType::getRetryDelayInSeconds,
                        CreateQualificationTypeRequest::getAutoGranted to QualificationType::getAutoGranted,
                        CreateQualificationTypeRequest::getAutoGrantedValue to QualificationType::getAutoGrantedValue
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonMTurk(context: IacContext) : BaseDeferredAmazonMTurk(context)
