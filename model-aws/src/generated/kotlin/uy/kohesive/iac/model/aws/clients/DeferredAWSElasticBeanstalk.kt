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

    override fun createConfigurationTemplate(request: CreateConfigurationTemplateRequest): CreateConfigurationTemplateResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateConfigurationTemplateRequest, CreateConfigurationTemplateResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateConfigurationTemplateRequest::getSolutionStackName to CreateConfigurationTemplateResult::getSolutionStackName,
                    CreateConfigurationTemplateRequest::getPlatformArn to CreateConfigurationTemplateResult::getPlatformArn,
                    CreateConfigurationTemplateRequest::getApplicationName to CreateConfigurationTemplateResult::getApplicationName,
                    CreateConfigurationTemplateRequest::getTemplateName to CreateConfigurationTemplateResult::getTemplateName,
                    CreateConfigurationTemplateRequest::getDescription to CreateConfigurationTemplateResult::getDescription,
                    CreateConfigurationTemplateRequest::getOptionSettings to CreateConfigurationTemplateResult::getOptionSettings
                )
            )
        }
    }

    override fun createEnvironment(request: CreateEnvironmentRequest): CreateEnvironmentResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateEnvironmentRequest, CreateEnvironmentResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateEnvironmentRequest::getEnvironmentName to CreateEnvironmentResult::getEnvironmentName,
                    CreateEnvironmentRequest::getApplicationName to CreateEnvironmentResult::getApplicationName,
                    CreateEnvironmentRequest::getVersionLabel to CreateEnvironmentResult::getVersionLabel,
                    CreateEnvironmentRequest::getSolutionStackName to CreateEnvironmentResult::getSolutionStackName,
                    CreateEnvironmentRequest::getPlatformArn to CreateEnvironmentResult::getPlatformArn,
                    CreateEnvironmentRequest::getTemplateName to CreateEnvironmentResult::getTemplateName,
                    CreateEnvironmentRequest::getDescription to CreateEnvironmentResult::getDescription,
                    CreateEnvironmentRequest::getTier to CreateEnvironmentResult::getTier
                )
            )
        }
    }

    override fun createPlatformVersion(request: CreatePlatformVersionRequest): CreatePlatformVersionResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePlatformVersionRequest, CreatePlatformVersionResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createStorageLocation(request: CreateStorageLocationRequest): CreateStorageLocationResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateStorageLocationRequest, CreateStorageLocationResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSElasticBeanstalk(context: IacContext) : BaseDeferredAWSElasticBeanstalk(context)
