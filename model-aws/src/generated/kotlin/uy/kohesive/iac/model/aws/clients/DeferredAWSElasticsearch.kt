package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticsearch.AbstractAWSElasticsearch
import com.amazonaws.services.elasticsearch.AWSElasticsearch
import com.amazonaws.services.elasticsearch.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSElasticsearch(val context: IacContext) : AbstractAWSElasticsearch(), AWSElasticsearch {


}

class DeferredAWSElasticsearch(context: IacContext) : BaseDeferredAWSElasticsearch(context)
