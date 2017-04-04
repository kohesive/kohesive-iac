package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.datapipeline.DataPipeline
import com.amazonaws.services.datapipeline.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface DataPipelineIdentifiable : KohesiveIdentifiable {

}

interface DataPipelineEnabled : DataPipelineIdentifiable {
    val dataPipelineClient: DataPipeline
    val dataPipelineContext: DataPipelineContext
    fun <T> withDataPipelineContext(init: DataPipelineContext.(DataPipeline) -> T): T = dataPipelineContext.init(dataPipelineClient)
}

open class BaseDataPipelineContext(protected val context: IacContext) : DataPipelineEnabled by context {

    open fun createPipeline(name: String, init: CreatePipelineRequest.() -> Unit): CreatePipelineResult {
        return dataPipelineClient.createPipeline(CreatePipelineRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class DataPipelineContext(context: IacContext) : BaseDataPipelineContext(context) {

}
