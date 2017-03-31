package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk
import com.amazonaws.services.elasticbeanstalk.AbstractAWSElasticBeanstalk
import com.amazonaws.services.elasticbeanstalk.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSElasticBeanstalk(val context: IacContext) : AbstractAWSElasticBeanstalk(), AWSElasticBeanstalk {

    override fun createApplication(request: CreateApplicationRequest): CreateApplicationResult {
        return with (context) {
            request.registerWithAutoName()
            CreateApplicationResult().withApplication(
                makeProxy<CreateApplicationRequest, ApplicationDescription>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateApplicationRequest::getApplicationName to ApplicationDescription::getApplicationName,
                        CreateApplicationRequest::getDescription to ApplicationDescription::getDescription,
                        CreateApplicationRequest::getResourceLifecycleConfig to ApplicationDescription::getResourceLifecycleConfig
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createApplicationVersion(request: CreateApplicationVersionRequest): CreateApplicationVersionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateApplicationVersionResult().withApplicationVersion(
                makeProxy<CreateApplicationVersionRequest, ApplicationVersionDescription>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateApplicationVersionRequest::getApplicationName to ApplicationVersionDescription::getApplicationName,
                        CreateApplicationVersionRequest::getDescription to ApplicationVersionDescription::getDescription,
                        CreateApplicationVersionRequest::getVersionLabel to ApplicationVersionDescription::getVersionLabel,
                        CreateApplicationVersionRequest::getSourceBuildInformation to ApplicationVersionDescription::getSourceBuildInformation,
                        CreateApplicationVersionRequest::getSourceBundle to ApplicationVersionDescription::getSourceBundle
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSElasticBeanstalk(context: IacContext) : BaseDeferredAWSElasticBeanstalk(context)
