package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.batch.AbstractAWSBatch
import com.amazonaws.services.batch.AWSBatch
import com.amazonaws.services.batch.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSBatch(val context: IacContext) : AbstractAWSBatch(), AWSBatch {

}

class DeferredAWSBatch(context: IacContext) : BaseDeferredAWSBatch(context)