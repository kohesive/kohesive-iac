package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object ElasticBeanstalk {

    @CloudFormationType("AWS::ElasticBeanstalk::Application")
    data class Application(
        val ApplicationName: String? = null,
        val Description: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::ElasticBeanstalk::ApplicationVersion")
    data class ApplicationVersion(
        val ApplicationName: String,
        val Description: String? = null,
        val SourceBundle: ApplicationVersion.SourceBundleProperty
    ) : ResourceProperties {

        data class SourceBundleProperty(
            val S3Bucket: String,
            val S3Key: String
        ) 

    }

    @CloudFormationType("AWS::ElasticBeanstalk::ConfigurationTemplate")
    data class ConfigurationTemplate(
        val ApplicationName: String,
        val Description: String? = null,
        val EnvironmentId: String? = null,
        val OptionSettings: List<ElasticBeanstalk.Environment.OptionSettingProperty>? = null,
        val SolutionStackName: String? = null,
        val SourceConfiguration: ConfigurationTemplate.SourceConfigurationProperty? = null
    ) : ResourceProperties {

        data class SourceConfigurationProperty(
            val ApplicationName: String,
            val TemplateName: String
        ) 

    }

    @CloudFormationType("AWS::ElasticBeanstalk::Environment")
    data class Environment(
        val ApplicationName: String,
        val CNAMEPrefix: String? = null,
        val Description: String? = null,
        val EnvironmentName: String? = null,
        val OptionSettings: List<ElasticBeanstalk.Environment.OptionSettingProperty>? = null,
        val SolutionStackName: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val TemplateName: String? = null,
        val Tier: Environment.TierProperty? = null,
        val VersionLabel: String? = null
    ) : ResourceProperties {

        data class OptionSettingProperty(
            val Namespace: String,
            val OptionName: String,
            val Value: String
        ) 


        data class TierProperty(
            val Name: String? = null,
            val Type: String? = null,
            val Version: String? = null
        ) 

    }


}