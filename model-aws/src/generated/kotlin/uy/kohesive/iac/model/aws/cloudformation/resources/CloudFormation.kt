package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object CloudFormation {

    @CloudFormationType("AWS::CloudFormation::ResourceTag")
    data class ResourceTag(
        val Key: String,
        val Value: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::CloudFormation::Authentication")
    data class Authentication(
        val accessKeyId: String? = null,
        val buckets: List<String>? = null,
        val password: String? = null,
        val secretKey: String? = null,
        val type: String,
        val uris: List<String>? = null,
        val username: String? = null,
        val roleName: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::CloudFormation::CustomResource")
    data class CustomResource(
        val ServiceToken: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::CloudFormation::Init")
    class Init : ResourceProperties 

    @CloudFormationType("AWS::CloudFormation::Interface")
    data class Interface(
        val ParameterGroups: Interface.ParameterGroupProperty? = null,
        val ParameterLabels: Interface.ParameterLabelProperty? = null
    ) : ResourceProperties {

        data class ParameterGroupProperty(
            val Label: Interface.ParameterGroupProperty.LabelProperty? = null,
            val Parameters: List<String>? = null
        ) {

            data class LabelProperty(
                val default: String? = null
            ) 

        }


        data class ParameterLabelProperty(
            val ParameterLogicalID: Interface.ParameterGroupProperty.LabelProperty? = null
        ) 

    }

    @CloudFormationType("AWS::CloudFormation::Stack")
    data class Stack(
        val NotificationARNs: List<String>? = null,
        val Parameters: Any? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val TemplateURL: String,
        val TimeoutInMinutes: String? = null
    ) : ResourceProperties {

        class ParameterProperty

    }

    @CloudFormationType("AWS::CloudFormation::WaitCondition")
    data class WaitCondition(
        val Count: String? = null,
        val Handle: String,
        val Timeout: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::CloudFormation::WaitConditionHandle")
    class WaitConditionHandle : ResourceProperties 


}