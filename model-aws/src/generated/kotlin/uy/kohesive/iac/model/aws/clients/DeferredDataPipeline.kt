package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.datapipeline.AbstractDataPipeline
import com.amazonaws.services.datapipeline.DataPipeline
import com.amazonaws.services.datapipeline.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredDataPipeline(val context: IacContext) : AbstractDataPipeline(), DataPipeline {

    override fun addTags(request: AddTagsRequest): AddTagsResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsResult().registerWithSameNameAs(request)
        }
    }

    override fun createPipeline(request: CreatePipelineRequest): CreatePipelineResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePipelineRequest, CreatePipelineResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredDataPipeline(context: IacContext) : BaseDeferredDataPipeline(context)
