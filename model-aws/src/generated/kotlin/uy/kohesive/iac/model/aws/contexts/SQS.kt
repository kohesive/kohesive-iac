package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface SQSIdentifiable : KohesiveIdentifiable {

}

interface SQSEnabled : SQSIdentifiable {
    val sqsClient: AmazonSQS
    val sqsContext: SQSContext
    fun <T> withSQSContext(init: SQSContext.(AmazonSQS) -> T): T = sqsContext.init(sqsClient)
}

open class BaseSQSContext(protected val context: IacContext) : SQSEnabled by context {

}

@DslScope
class SQSContext(context: IacContext) : BaseSQSContext(context) {

}