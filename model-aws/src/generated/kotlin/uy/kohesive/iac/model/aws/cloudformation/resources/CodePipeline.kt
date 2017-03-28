package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object CodePipeline {

    @CloudFormationType("AWS::CodePipeline::CustomActionType")
    data class CustomActionType(
        val Category: String,
        val ConfigurationProperties: List<CodePipeline.CustomActionType.ConfigurationPropertyProperty>? = null,
        val InputArtifactDetails: CustomActionType.ArtifactDetailProperty,
        val OutputArtifactDetails: CustomActionType.ArtifactDetailProperty,
        val Provider: String,
        val Settings: CustomActionType.SettingProperty? = null,
        val Version: String? = null
    ) : ResourceProperties {

        data class ConfigurationPropertyProperty(
            val Description: String? = null,
            val Key: String,
            val Name: String,
            val Queryable: String? = null,
            val Required: String,
            val Secret: String,
            val Type: String? = null
        ) 


        data class ArtifactDetailProperty(
            val MaximumCount: String,
            val MinimumCount: String
        ) 


        data class SettingProperty(
            val EntityUrlTemplate: String? = null,
            val ExecutionUrlTemplate: String? = null,
            val RevisionUrlTemplate: String? = null,
            val ThirdPartyConfigurationUrl: String? = null
        ) 

    }

    @CloudFormationType("AWS::CodePipeline::Pipeline")
    data class Pipeline(
        val ArtifactStore: Pipeline.ArtifactStoreProperty,
        val DisableInboundStageTransitions: List<CodePipeline.Pipeline.DisableInboundStageTransitionProperty>? = null,
        val Name: String? = null,
        val RestartExecutionOnUpdate: String? = null,
        val RoleArn: String,
        val Stages: List<CodePipeline.Pipeline.StageProperty>
    ) : ResourceProperties {

        data class ArtifactStoreProperty(
            val EncryptionKey: Pipeline.ArtifactStoreProperty.EncryptionKeyProperty? = null,
            val Location: String,
            val Type: String
        ) {

            data class EncryptionKeyProperty(
                val Id: String,
                val Type: String
            ) 

        }


        data class DisableInboundStageTransitionProperty(
            val Reason: String,
            val StageName: String
        ) 


        data class StageProperty(
            val Actions: List<CodePipeline.Pipeline.StageProperty.ActionProperty>,
            val Blockers: List<CodePipeline.Pipeline.StageProperty.BlockerProperty>? = null,
            val Name: String
        ) {

            data class ActionProperty(
                val ActionTypeId: Pipeline.StageProperty.ActionProperty.ActionTypeIdProperty,
                val Configuration: Any? = null,
                val InputArtifacts: List<CodePipeline.Pipeline.StageProperty.ActionProperty.InputArtifactProperty>? = null,
                val Name: String,
                val OutputArtifacts: List<CodePipeline.Pipeline.StageProperty.ActionProperty.OutputArtifactProperty>? = null,
                val RoleArn: String? = null,
                val RunOrder: String? = null
            ) {

                data class ActionTypeIdProperty(
                    val Category: String,
                    val Owner: String,
                    val Provider: String,
                    val Version: String
                ) 


                data class InputArtifactProperty(
                    val Name: String
                ) 


                data class OutputArtifactProperty(
                    val Name: String
                ) 

            }


            data class BlockerProperty(
                val Name: String,
                val Type: String
            ) 

        }

    }


}