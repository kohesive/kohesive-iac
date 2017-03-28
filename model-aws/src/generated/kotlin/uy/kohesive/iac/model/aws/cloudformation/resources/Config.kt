package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object Config {

    @CloudFormationType("AWS::Config::ConfigRule")
    data class ConfigRule(
        val ConfigRuleName: String? = null,
        val Description: String? = null,
        val InputParameters: Any? = null,
        val MaximumExecutionFrequency: String? = null,
        val Scope: ConfigRule.ScopeProperty? = null,
        val Source: ConfigRule.SourceProperty
    ) : ResourceProperties {

        data class ScopeProperty(
            val ComplianceResourceId: String? = null,
            val ComplianceResourceTypes: List<String>? = null,
            val TagKey: String? = null,
            val TagValue: String? = null
        ) 


        data class SourceProperty(
            val Owner: String,
            val SourceDetails: List<Config.ConfigRule.SourceProperty.SourceDetailProperty>? = null,
            val SourceIdentifier: String
        ) {

            data class SourceDetailProperty(
                val EventSource: String,
                val MessageType: String
            ) 

        }

    }

    @CloudFormationType("AWS::Config::ConfigurationRecorder")
    data class ConfigurationRecorder(
        val Name: String? = null,
        val RecordingGroup: ConfigurationRecorder.RecordingGroupProperty? = null,
        val RoleARN: String
    ) : ResourceProperties {

        data class RecordingGroupProperty(
            val AllSupported: String? = null,
            val IncludeGlobalResourceTypes: String? = null,
            val ResourceTypes: List<String>? = null
        ) 

    }

    @CloudFormationType("AWS::Config::DeliveryChannel")
    data class DeliveryChannel(
        val ConfigSnapshotDeliveryProperties: DeliveryChannel.ConfigSnapshotDeliveryPropertyProperty? = null,
        val Name: String? = null,
        val S3BucketName: String,
        val S3KeyPrefix: String? = null,
        val SnsTopicARN: String? = null
    ) : ResourceProperties {

        data class ConfigSnapshotDeliveryPropertyProperty(
            val DeliveryFrequency: String? = null
        ) 

    }


}