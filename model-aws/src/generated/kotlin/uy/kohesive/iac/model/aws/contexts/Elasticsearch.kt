package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.elasticsearch.AWSElasticsearch
import com.amazonaws.services.elasticsearch.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ElasticsearchIdentifiable : KohesiveIdentifiable {

}

interface ElasticsearchEnabled : ElasticsearchIdentifiable {
    val elasticsearchClient: AWSElasticsearch
    val elasticsearchContext: ElasticsearchContext
    fun <T> withElasticsearchContext(init: ElasticsearchContext.(AWSElasticsearch) -> T): T = elasticsearchContext.init(elasticsearchClient)
}

open class BaseElasticsearchContext(protected val context: IacContext) : ElasticsearchEnabled by context {

}

@DslScope
class ElasticsearchContext(context: IacContext) : BaseElasticsearchContext(context) {

}