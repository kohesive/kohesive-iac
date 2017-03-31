package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.sqs.AbstractAmazonSQS
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonSQS(val context: IacContext) : AbstractAmazonSQS(), AmazonSQS {

    override fun addPermission(request: AddPermissionRequest): AddPermissionResult {
        return with (context) {
            request.registerWithAutoName()
            AddPermissionResult().registerWithSameNameAs(request)
        }
    }

    override fun createQueue(request: CreateQueueRequest): CreateQueueResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateQueueRequest, CreateQueueResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonSQS(context: IacContext) : BaseDeferredAmazonSQS(context)
