package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface SNSIdentifiable : KohesiveIdentifiable {

}

interface SNSEnabled : SNSIdentifiable {
    val snsClient: AmazonSNS
    val snsContext: SNSContext
    fun <T> withSNSContext(init: SNSContext.(AmazonSNS) -> T): T = snsContext.init(snsClient)
}

open class BaseSNSContext(protected val context: IacContext) : SNSEnabled by context {

    open fun createPlatformApplication(name: String, init: CreatePlatformApplicationRequest.() -> Unit): CreatePlatformApplicationResult {
        return snsClient.createPlatformApplication(CreatePlatformApplicationRequest().apply {
            withName(name)
            init()
        })
    }

    open fun createTopic(name: String, init: CreateTopicRequest.() -> Unit): CreateTopicResult {
        return snsClient.createTopic(CreateTopicRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class SNSContext(context: IacContext) : BaseSNSContext(context) {

}
