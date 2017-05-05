package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.codebuild.model.CreateProjectRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.CodeBuild

class CodeBuildProjectResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateProjectRequest> {

    override val requestClazz = CreateProjectRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateProjectRequest).let {
            CodeBuild.Project(
                Artifacts = request.artifacts.let {
                    CodeBuild.Project.ArtifactProperty(
                        Location      = it.location,
                        Type          = it.type,
                        Path          = it.path,
                        Name          = it.name,
                        NamespaceType = it.namespaceType,
                        Packaging     = it.packaging
                    )
                },
                Description   = request.description,
                EncryptionKey = request.encryptionKey,
                Environment   = request.environment.let {
                    CodeBuild.Project.EnvironmentProperty(
                        ComputeType          = it.computeType,
                        Type                 = it.type,
                        EnvironmentVariables = it.environmentVariables?.map {
                            CodeBuild.Project.EnvironmentProperty.EnvironmentVariableProperty(
                                Name  = it.name,
                                Value = it.value
                            )
                        },
                        Image = it.image
                    )
                },
                Name        = request.name,
                ServiceRole = request.serviceRole,
                Source      = request.source.let {
                    CodeBuild.Project.SourceProperty(
                        BuildSpec = it.buildspec,
                        Type      = it.type,
                        Location  = it.location
                    )
                },
                Tags = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                },
                TimeoutInMinutes = request.timeoutInMinutes?.toString()
            )
        }

}

