package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationRequest
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationVersionRequest
import com.amazonaws.services.elasticbeanstalk.model.CreateConfigurationTemplateRequest
import com.amazonaws.services.elasticbeanstalk.model.CreateEnvironmentRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.ElasticBeanstalk

class ElasticBeanstalkApplicationResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateApplicationRequest> {

    override val requestClazz = CreateApplicationRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateApplicationRequest).let {
            ElasticBeanstalk.Application(
                ApplicationName = request.applicationName,
                Description     = request.description
            )
        }

}

class ElasticBeanstalkApplicationVersionResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateApplicationVersionRequest> {

    override val requestClazz = CreateApplicationVersionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateApplicationVersionRequest).let {
            ElasticBeanstalk.ApplicationVersion(
                ApplicationName = request.applicationName,
                Description     = request.description,
                SourceBundle    = request.sourceBundle.let {
                    ElasticBeanstalk.ApplicationVersion.SourceBundleProperty(
                        S3Bucket = it.s3Bucket,
                        S3Key    = it.s3Key
                    )
                }
            )
        }

}

class ElasticBeanstalkConfigurationTemplateResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateConfigurationTemplateRequest> {

    override val requestClazz = CreateConfigurationTemplateRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateConfigurationTemplateRequest).let {
            ElasticBeanstalk.ConfigurationTemplate(
                ApplicationName     = request.applicationName,
                Description         = request.description,
                EnvironmentId       = request.environmentId,
                OptionSettings      = request.optionSettings?.map {
                    ElasticBeanstalk.Environment.OptionSettingProperty(
                        Namespace  = it.namespace,
                        Value      = it.value,
                        OptionName = it.optionName
                    )
                },
                SolutionStackName   = request.solutionStackName,
                SourceConfiguration = request.sourceConfiguration?.let {
                    ElasticBeanstalk.ConfigurationTemplate.SourceConfigurationProperty(
                        ApplicationName = it.applicationName,
                        TemplateName    = it.templateName
                    )
                }
            )
        }

}

class ElasticBeanstalkEnvironmentResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateEnvironmentRequest> {

    override val requestClazz = CreateEnvironmentRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateEnvironmentRequest).let {
            ElasticBeanstalk.Environment(
                ApplicationName   = request.applicationName,
                CNAMEPrefix       = request.cnamePrefix,
                Description       = request.description,
                EnvironmentName   = request.environmentName,
                OptionSettings    = request.optionSettings?.map {
                    ElasticBeanstalk.Environment.OptionSettingProperty(
                        Namespace  = it.namespace,
                        OptionName = it.optionName,
                        Value      = it.value
                    )
                },
                SolutionStackName = request.solutionStackName,
                Tags              = request.tags.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                },
                TemplateName = request.templateName,
                Tier         = request.tier?.let {
                    ElasticBeanstalk.Environment.TierProperty(
                        Name    = it.name,
                        Type    = it.type,
                        Version = it.version
                    )
                },
                VersionLabel = request.versionLabel
            )
        }

}

