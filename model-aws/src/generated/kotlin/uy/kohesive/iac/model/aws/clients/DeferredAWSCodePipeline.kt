package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.codepipeline.AbstractAWSCodePipeline
import com.amazonaws.services.codepipeline.AWSCodePipeline
import com.amazonaws.services.codepipeline.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCodePipeline(val context: IacContext) : AbstractAWSCodePipeline(), AWSCodePipeline {

    override fun createCustomActionType(request: CreateCustomActionTypeRequest): CreateCustomActionTypeResult {
        return with (context) {
            request.registerWithAutoName()
            CreateCustomActionTypeResult().withActionType(
                makeProxy<CreateCustomActionTypeRequest, ActionType>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateCustomActionTypeRequest::getSettings to ActionType::getSettings,
                        CreateCustomActionTypeRequest::getInputArtifactDetails to ActionType::getInputArtifactDetails,
                        CreateCustomActionTypeRequest::getOutputArtifactDetails to ActionType::getOutputArtifactDetails
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSCodePipeline(context: IacContext) : BaseDeferredAWSCodePipeline(context)
