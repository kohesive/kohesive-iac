package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk
import com.amazonaws.services.elasticbeanstalk.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ElasticBeanstalkIdentifiable : KohesiveIdentifiable {

}

interface ElasticBeanstalkEnabled : ElasticBeanstalkIdentifiable {
    val elasticBeanstalkClient: AWSElasticBeanstalk
    val elasticBeanstalkContext: ElasticBeanstalkContext
    fun <T> withElasticBeanstalkContext(init: ElasticBeanstalkContext.(AWSElasticBeanstalk) -> T): T = elasticBeanstalkContext.init(elasticBeanstalkClient)
}

open class BaseElasticBeanstalkContext(protected val context: IacContext) : ElasticBeanstalkEnabled by context {

}

@DslScope
class ElasticBeanstalkContext(context: IacContext) : BaseElasticBeanstalkContext(context) {

}