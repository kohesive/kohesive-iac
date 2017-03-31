package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.kinesis.AbstractAmazonKinesis
import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonKinesis(val context: IacContext) : AbstractAmazonKinesis(), AmazonKinesis {

    override fun addTagsToStream(request: AddTagsToStreamRequest): AddTagsToStreamResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsToStreamResult().registerWithSameNameAs(request)
        }
    }

    override fun createStream(request: CreateStreamRequest): CreateStreamResult {
        return with (context) {
            request.registerWithAutoName()
            CreateStreamResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonKinesis(context: IacContext) : BaseDeferredAmazonKinesis(context)
