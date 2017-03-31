package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticsearch.AbstractAWSElasticsearch
import com.amazonaws.services.elasticsearch.AWSElasticsearch
import com.amazonaws.services.elasticsearch.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSElasticsearch(val context: IacContext) : AbstractAWSElasticsearch(), AWSElasticsearch {

    override fun addTags(request: AddTagsRequest): AddTagsResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsResult().registerWithSameNameAs(request)
        }
    }

    override fun createElasticsearchDomain(request: CreateElasticsearchDomainRequest): CreateElasticsearchDomainResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateElasticsearchDomainRequest, CreateElasticsearchDomainResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSElasticsearch(context: IacContext) : BaseDeferredAWSElasticsearch(context)
