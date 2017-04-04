package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose
import com.amazonaws.services.kinesisfirehose.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface KinesisFirehoseIdentifiable : KohesiveIdentifiable {

}

interface KinesisFirehoseEnabled : KinesisFirehoseIdentifiable {
    val kinesisFirehoseClient: AmazonKinesisFirehose
    val kinesisFirehoseContext: KinesisFirehoseContext
    fun <T> withKinesisFirehoseContext(init: KinesisFirehoseContext.(AmazonKinesisFirehose) -> T): T = kinesisFirehoseContext.init(kinesisFirehoseClient)
}

open class BaseKinesisFirehoseContext(protected val context: IacContext) : KinesisFirehoseEnabled by context {

    open fun createDeliveryStream(deliveryStreamName: String, init: CreateDeliveryStreamRequest.() -> Unit): CreateDeliveryStreamResult {
        return kinesisFirehoseClient.createDeliveryStream(CreateDeliveryStreamRequest().apply {
            withDeliveryStreamName(deliveryStreamName)
            init()
        })
    }


}

@DslScope
class KinesisFirehoseContext(context: IacContext) : BaseKinesisFirehoseContext(context) {

}
