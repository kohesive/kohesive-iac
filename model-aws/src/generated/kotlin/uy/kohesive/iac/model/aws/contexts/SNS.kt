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

}

@DslScope
class SNSContext(context: IacContext) : BaseSNSContext(context) {

}