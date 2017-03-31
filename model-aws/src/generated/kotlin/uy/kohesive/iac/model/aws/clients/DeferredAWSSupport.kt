package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.support.AbstractAWSSupport
import com.amazonaws.services.support.AWSSupport
import com.amazonaws.services.support.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSSupport(val context: IacContext) : AbstractAWSSupport(), AWSSupport {

    override fun addAttachmentsToSet(request: AddAttachmentsToSetRequest): AddAttachmentsToSetResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddAttachmentsToSetRequest, AddAttachmentsToSetResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    AddAttachmentsToSetRequest::getAttachmentSetId to AddAttachmentsToSetResult::getAttachmentSetId
                )
            )
        }
    }

    override fun addCommunicationToCase(request: AddCommunicationToCaseRequest): AddCommunicationToCaseResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddCommunicationToCaseRequest, AddCommunicationToCaseResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createCase(request: CreateCaseRequest): CreateCaseResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateCaseRequest, CreateCaseResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSSupport(context: IacContext) : BaseDeferredAWSSupport(context)
