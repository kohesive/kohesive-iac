package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticbeanstalk.AbstractAWSElasticBeanstalk
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk
import com.amazonaws.services.elasticbeanstalk.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSElasticBeanstalk(val context: IacContext) : AbstractAWSElasticBeanstalk(), AWSElasticBeanstalk {

}

class DeferredAWSElasticBeanstalk(context: IacContext) : BaseDeferredAWSElasticBeanstalk(context)