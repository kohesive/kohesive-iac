package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface KinesisIdentifiable : KohesiveIdentifiable {

}

interface KinesisEnabled : KinesisIdentifiable {
    val kinesisClient: AmazonKinesis
    val kinesisContext: KinesisContext
    fun <T> withKinesisContext(init: KinesisContext.(AmazonKinesis) -> T): T = kinesisContext.init(kinesisClient)
}

open class BaseKinesisContext(protected val context: IacContext) : KinesisEnabled by context {

    fun createStream(streamName: String, init: CreateStreamRequest.() -> Unit): CreateStreamResult {
        return kinesisClient.createStream(CreateStreamRequest().apply {
            withStreamName(streamName)
            init()
        })
    }


}

@DslScope
class KinesisContext(context: IacContext) : BaseKinesisContext(context) {

}
