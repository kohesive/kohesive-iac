package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.config.model.PutConfigRuleRequest
import com.amazonaws.services.config.model.PutConfigurationRecorderRequest
import com.amazonaws.services.config.model.PutDeliveryChannelRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.Config
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class ConfigConfigRuleResourcePropertiesBuilder : ResourcePropertiesBuilder<PutConfigRuleRequest> {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    override val requestClazz = PutConfigRuleRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutConfigRuleRequest).configRule.let {
            Config.ConfigRule(
                ConfigRuleName  = it.configRuleName,
                Description     = it.description,
                InputParameters = it.inputParameters?.let {
                    JSON.readValue<Map<String, Any>>(it)
                },
                MaximumExecutionFrequency = it.maximumExecutionFrequency,
                Scope = it.scope?.let {
                    Config.ConfigRule.ScopeProperty(
                        ComplianceResourceId    = it.complianceResourceId,
                        ComplianceResourceTypes = it.complianceResourceTypes,
                        TagKey                  = it.tagKey,
                        TagValue                = it.tagValue
                    )
                },
                Source = it.source.let {
                    Config.ConfigRule.SourceProperty(
                        Owner         = it.owner,
                        SourceDetails = it.sourceDetails?.map {
                            Config.ConfigRule.SourceProperty.SourceDetailProperty(
                                EventSource = it.eventSource,
                                MessageType = it.messageType
                            )
                        },
                        SourceIdentifier = it.sourceIdentifier
                    )
                }
            )
        }

}

class ConfigConfigurationRecorderResourcePropertiesBuilder : ResourcePropertiesBuilder<PutConfigurationRecorderRequest> {

    override val requestClazz = PutConfigurationRecorderRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutConfigurationRecorderRequest).configurationRecorder.let {
            Config.ConfigurationRecorder(
                Name           = it.name,
                RoleARN        = it.roleARN,
                RecordingGroup = it.recordingGroup?.let {
                    Config.ConfigurationRecorder.RecordingGroupProperty(
                        AllSupported               = it.allSupported?.toString(),
                        IncludeGlobalResourceTypes = it.includeGlobalResourceTypes?.toString(),
                        ResourceTypes              = it.resourceTypes
                    )
                }
            )
        }

}

class ConfigDeliveryChannelResourcePropertiesBuilder : ResourcePropertiesBuilder<PutDeliveryChannelRequest> {

    override val requestClazz = PutDeliveryChannelRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutDeliveryChannelRequest).deliveryChannel.let {
            Config.DeliveryChannel(
                ConfigSnapshotDeliveryProperties = it.configSnapshotDeliveryProperties?.let {
                    Config.DeliveryChannel.ConfigSnapshotDeliveryPropertyProperty(
                        DeliveryFrequency = it.deliveryFrequency
                    )
                },
                Name         = it.name,
                S3BucketName = it.s3BucketName,
                S3KeyPrefix  = it.s3KeyPrefix,
                SnsTopicARN  = it.snsTopicARN
            )
        }

}

