package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.codepipeline.model.CreateCustomActionTypeRequest
import com.amazonaws.services.codepipeline.model.CreatePipelineRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CodePipeline

class CodePipelineCustomActionTypeResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateCustomActionTypeRequest> {

    override val requestClazz = CreateCustomActionTypeRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateCustomActionTypeRequest).let {
            CodePipeline.CustomActionType(
                Category                = request.category,
                ConfigurationProperties = request.configurationProperties?.map {
                    CodePipeline.CustomActionType.ConfigurationPropertyProperty(
                        Description = it.description,
                        Key         = it.key.toString(),
                        Type        = it.type,
                        Name        = it.name,
                        Queryable   = it.queryable?.toString(),
                        Required    = it.required.toString(),
                        Secret      = it.secret.toString()
                    )
                },
                InputArtifactDetails = request.inputArtifactDetails.let {
                    CodePipeline.CustomActionType.ArtifactDetailProperty(
                        MaximumCount = it.maximumCount.toString(),
                        MinimumCount = it.minimumCount.toString()
                    )
                },
                OutputArtifactDetails = request.inputArtifactDetails.let {
                    CodePipeline.CustomActionType.ArtifactDetailProperty(
                        MaximumCount = it.maximumCount.toString(),
                        MinimumCount = it.minimumCount.toString()
                    )
                },
                Provider = request.provider,
                Settings = request.settings?.let {
                    CodePipeline.CustomActionType.SettingProperty(
                        EntityUrlTemplate          = it.entityUrlTemplate,
                        ExecutionUrlTemplate       = it.executionUrlTemplate,
                        RevisionUrlTemplate        = it.revisionUrlTemplate,
                        ThirdPartyConfigurationUrl = it.thirdPartyConfigurationUrl
                    )
                },
                Version = request.version
            )
        }

}

class CodePipelinePipelineResourcePropertiesBuilder : ResourcePropertiesBuilder<CreatePipelineRequest> {

    override val requestClazz = CreatePipelineRequest::class

    // TODO: missing properties - 'DisableInboundStageTransitions', 'RestartExecutionOnUpdate'
    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreatePipelineRequest).pipeline.let {
            CodePipeline.Pipeline(
                ArtifactStore = it.artifactStore.let {
                    CodePipeline.Pipeline.ArtifactStoreProperty(
                        EncryptionKey = it.encryptionKey?.let {
                            CodePipeline.Pipeline.ArtifactStoreProperty.EncryptionKeyProperty(
                                Id   = it.id,
                                Type = it.type
                            )
                        },
                        Type     = it.type,
                        Location = it.location
                    )
                },
                Name    = it.name,
                RoleArn = it.roleArn,
                Stages  = it.stages.map {
                    CodePipeline.Pipeline.StageProperty(
                        Actions = it.actions.map {
                            CodePipeline.Pipeline.StageProperty.ActionProperty(
                                ActionTypeId = it.actionTypeId.let {
                                    CodePipeline.Pipeline.StageProperty.ActionProperty.ActionTypeIdProperty(
                                        Category = it.category,
                                        Version  = it.version,
                                        Owner    = it.owner,
                                        Provider = it.provider
                                    )
                                },
                                RoleArn        = it.roleArn,
                                Name           = it.name,
                                Configuration  = it.configuration,
                                InputArtifacts = it.inputArtifacts?.map {
                                    CodePipeline.Pipeline.StageProperty.ActionProperty.InputArtifactProperty(
                                        Name = it.name
                                    )
                                },
                                OutputArtifacts = it.outputArtifacts?.map {
                                    CodePipeline.Pipeline.StageProperty.ActionProperty.OutputArtifactProperty(
                                        Name = it.name
                                    )
                                },
                                RunOrder = it.runOrder?.toString()
                            )
                        },
                        Name     = it.name,
                        Blockers = it.blockers?.map {
                            CodePipeline.Pipeline.StageProperty.BlockerProperty(
                                Name = it.name,
                                Type = it.type
                            )
                        }
                    )
                }
            )
        }

}

