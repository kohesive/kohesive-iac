package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.kinesisfirehose.AbstractAmazonKinesisFirehose
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose
import com.amazonaws.services.kinesisfirehose.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonKinesisFirehose(val context: IacContext) : AbstractAmazonKinesisFirehose(), AmazonKinesisFirehose {

    override fun createDeliveryStream(request: CreateDeliveryStreamRequest): CreateDeliveryStreamResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDeliveryStreamRequest, CreateDeliveryStreamResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonKinesisFirehose(context: IacContext) : BaseDeferredAmazonKinesisFirehose(context)
