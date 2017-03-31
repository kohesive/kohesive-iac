package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk
import com.amazonaws.services.elasticbeanstalk.model.ApplicationDescription
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationRequest
import com.amazonaws.services.elasticbeanstalk.model.CreateEnvironmentRequest
import com.amazonaws.services.elasticbeanstalk.model.CreateEnvironmentResult
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

    fun createApplication(applicationName: String, init: CreateApplicationRequest.() -> Unit): ApplicationDescription {
        return elasticBeanstalkClient.createApplication(CreateApplicationRequest().apply {
            withApplicationName(applicationName)
            init()
        }).application
    }

    fun createEnvironment(environmentName: String, init: CreateEnvironmentRequest.() -> Unit): CreateEnvironmentResult {
        return elasticBeanstalkClient.createEnvironment(CreateEnvironmentRequest().apply {
            withEnvironmentName(environmentName)
            init()
        })
    }


}

@DslScope
class ElasticBeanstalkContext(context: IacContext) : BaseElasticBeanstalkContext(context) {

}
