package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.batch.AWSBatch
import com.amazonaws.services.batch.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface BatchIdentifiable : KohesiveIdentifiable {

}

interface BatchEnabled : BatchIdentifiable {
    val batchClient: AWSBatch
    val batchContext: BatchContext
    fun <T> withBatchContext(init: BatchContext.(AWSBatch) -> T): T = batchContext.init(batchClient)
}

open class BaseBatchContext(protected val context: IacContext) : BatchEnabled by context {

}

@DslScope
class BatchContext(context: IacContext) : BaseBatchContext(context) {

}