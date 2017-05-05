package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.codedeploy.model.CreateApplicationRequest
import com.amazonaws.services.codedeploy.model.CreateDeploymentConfigRequest
import com.amazonaws.services.codedeploy.model.CreateDeploymentGroupRequest
import com.amazonaws.services.codedeploy.model.CreateDeploymentRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CodeDeploy

class CodeDeployApplicationResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateApplicationRequest> {

    override val requestClazz = CreateApplicationRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateApplicationRequest).let {
            CodeDeploy.Application(
                ApplicationName = request.applicationName
            )
        }

}

class CodeDeployDeploymentConfigResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDeploymentConfigRequest> {

    override val requestClazz = CreateDeploymentConfigRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDeploymentConfigRequest).let {
            CodeDeploy.DeploymentConfig(
                DeploymentConfigName = request.deploymentConfigName,
                MinimumHealthyHosts  = request.minimumHealthyHosts?.let {
                    CodeDeploy.DeploymentConfig.MinimumHealthyHostProperty(
                        Type  = it.type,
                        Value = it.value?.toString()
                    )
                }
            )
        }

}

class CodeDeployDeploymentGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDeploymentGroupRequest> {

    override val requestClazz = CreateDeploymentGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDeploymentGroupRequest).let {
            CodeDeploy.DeploymentGroup(
                ApplicationName      = request.applicationName,
                AutoScalingGroups    = request.autoScalingGroups,
                DeploymentConfigName = request.deploymentConfigName,
                DeploymentGroupName  = request.deploymentGroupName,
                Ec2TagFilters        = request.ec2TagFilters.map {
                    CodeDeploy.DeploymentGroup.Ec2TagFilterProperty(
                        Key   = it.key,
                        Value = it.value,
                        Type  = it.type
                    )
                },
                OnPremisesInstanceTagFilters = request.onPremisesInstanceTagFilters?.map {
                    CodeDeploy.DeploymentGroup.OnPremisesInstanceTagFilterProperty(
                        Key   = it.key,
                        Value = it.value
                    )
                },
                ServiceRoleArn = request.serviceRoleArn,
                Deployment     = relatedObjects.filterIsInstance<CreateDeploymentRequest>().firstOrNull()?.let {
                    CodeDeploy.DeploymentGroup.DeploymentProperty(
                        Description                   = it.description,
                        IgnoreApplicationStopFailures = it.ignoreApplicationStopFailures?.toString(),
                        Revision                      = it.revision.let {
                            CodeDeploy.DeploymentGroup.RevisionProperty(
                                GitHubLocation = it.gitHubLocation?.let {
                                    CodeDeploy.DeploymentGroup.RevisionProperty.GitHubLocationProperty(
                                        CommitId   = it.commitId,
                                        Repository = it.repository
                                    )
                                },
                                S3Location = it.s3Location?.let {
                                    CodeDeploy.DeploymentGroup.RevisionProperty.S3LocationProperty(
                                        Bucket     = it.bucket,
                                        Key        = it.key,
                                        BundleType = it.bundleType,
                                        ETag       = it.eTag,
                                        Version    = it.version
                                    )
                                },
                                RevisionType = it.revisionType
                            )
                        }
                    )
                }
            )
        }

}

