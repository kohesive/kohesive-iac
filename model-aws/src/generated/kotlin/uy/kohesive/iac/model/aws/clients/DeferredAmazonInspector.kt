package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.inspector.AbstractAmazonInspector
import com.amazonaws.services.inspector.AmazonInspector
import com.amazonaws.services.inspector.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonInspector(val context: IacContext) : AbstractAmazonInspector(), AmazonInspector {

    override fun addAttributesToFindings(request: AddAttributesToFindingsRequest): AddAttributesToFindingsResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddAttributesToFindingsRequest, AddAttributesToFindingsResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createAssessmentTarget(request: CreateAssessmentTargetRequest): CreateAssessmentTargetResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateAssessmentTargetRequest, CreateAssessmentTargetResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createAssessmentTemplate(request: CreateAssessmentTemplateRequest): CreateAssessmentTemplateResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateAssessmentTemplateRequest, CreateAssessmentTemplateResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createResourceGroup(request: CreateResourceGroupRequest): CreateResourceGroupResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateResourceGroupRequest, CreateResourceGroupResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonInspector(context: IacContext) : BaseDeferredAmazonInspector(context)
