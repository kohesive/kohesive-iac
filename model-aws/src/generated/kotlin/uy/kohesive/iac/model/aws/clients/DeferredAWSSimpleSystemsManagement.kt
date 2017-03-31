package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.simplesystemsmanagement.AbstractAWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSSimpleSystemsManagement(val context: IacContext) : AbstractAWSSimpleSystemsManagement(), AWSSimpleSystemsManagement {

    override fun createAssociation(request: CreateAssociationRequest): CreateAssociationResult {
        return with (context) {
            request.registerWithAutoName()
            CreateAssociationResult().withAssociationDescription(
                makeProxy<CreateAssociationRequest, AssociationDescription>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateAssociationRequest::getName to AssociationDescription::getName,
                        CreateAssociationRequest::getInstanceId to AssociationDescription::getInstanceId,
                        CreateAssociationRequest::getDocumentVersion to AssociationDescription::getDocumentVersion,
                        CreateAssociationRequest::getParameters to AssociationDescription::getParameters,
                        CreateAssociationRequest::getTargets to AssociationDescription::getTargets,
                        CreateAssociationRequest::getScheduleExpression to AssociationDescription::getScheduleExpression,
                        CreateAssociationRequest::getOutputLocation to AssociationDescription::getOutputLocation
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createDocument(request: CreateDocumentRequest): CreateDocumentResult {
        return with (context) {
            request.registerWithAutoName()
            CreateDocumentResult().withDocumentDescription(
                makeProxy<CreateDocumentRequest, DocumentDescription>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateDocumentRequest::getName to DocumentDescription::getName,
                        CreateDocumentRequest::getDocumentType to DocumentDescription::getDocumentType
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSSimpleSystemsManagement(context: IacContext) : BaseDeferredAWSSimpleSystemsManagement(context)
