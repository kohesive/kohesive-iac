package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder
import com.amazonaws.services.elastictranscoder.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ElasticTranscoderIdentifiable : KohesiveIdentifiable {

}

interface ElasticTranscoderEnabled : ElasticTranscoderIdentifiable {
    val elasticTranscoderClient: AmazonElasticTranscoder
    val elasticTranscoderContext: ElasticTranscoderContext
    fun <T> withElasticTranscoderContext(init: ElasticTranscoderContext.(AmazonElasticTranscoder) -> T): T = elasticTranscoderContext.init(elasticTranscoderClient)
}

open class BaseElasticTranscoderContext(protected val context: IacContext) : ElasticTranscoderEnabled by context {

    fun createPipeline(name: String, init: CreatePipelineRequest.() -> Unit): Pipeline {
        return elasticTranscoderClient.createPipeline(CreatePipelineRequest().apply {
            withName(name)
            init()
        }).pipeline
    }

    fun createPreset(name: String, init: CreatePresetRequest.() -> Unit): Preset {
        return elasticTranscoderClient.createPreset(CreatePresetRequest().apply {
            withName(name)
            init()
        }).preset
    }


}

@DslScope
class ElasticTranscoderContext(context: IacContext) : BaseElasticTranscoderContext(context) {

}
