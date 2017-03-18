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

}

@DslScope
class ElasticTranscoderContext(context: IacContext) : BaseElasticTranscoderContext(context) {

}