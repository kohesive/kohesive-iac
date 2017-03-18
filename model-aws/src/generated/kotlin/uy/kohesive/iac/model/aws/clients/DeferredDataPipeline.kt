package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.datapipeline.AbstractDataPipeline
import com.amazonaws.services.datapipeline.DataPipeline
import com.amazonaws.services.datapipeline.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredDataPipeline(val context: IacContext) : AbstractDataPipeline(), DataPipeline {

}

class DeferredDataPipeline(context: IacContext) : BaseDeferredDataPipeline(context)