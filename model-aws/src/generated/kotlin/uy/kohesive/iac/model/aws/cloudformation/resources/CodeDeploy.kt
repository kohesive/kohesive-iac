package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object CodeDeploy {

    @CloudFormationType("AWS::CodeDeploy::Application")
    data class Application(
        val ApplicationName: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::CodeDeploy::DeploymentConfig")
    data class DeploymentConfig(
        val DeploymentConfigName: String? = null,
        val MinimumHealthyHosts: DeploymentConfig.MinimumHealthyHostProperty? = null
    ) : ResourceProperties {

        data class MinimumHealthyHostProperty(
            val Type: String? = null,
            val Value: String? = null
        ) 

    }

    @CloudFormationType("AWS::CodeDeploy::DeploymentGroup")
    data class DeploymentGroup(
        val ApplicationName: String,
        val AutoScalingGroups: List<String>? = null,
        val Deployment: DeploymentGroup.DeploymentProperty? = null,
        val DeploymentConfigName: String? = null,
        val DeploymentGroupName: String? = null,
        val Ec2TagFilters: List<CodeDeploy.DeploymentGroup.Ec2TagFilterProperty>? = null,
        val OnPremisesInstanceTagFilters: List<CodeDeploy.DeploymentGroup.OnPremisesInstanceTagFilterProperty>? = null,
        val ServiceRoleArn: String
    ) : ResourceProperties {

        data class DeploymentProperty(
            val Description: String? = null,
            val IgnoreApplicationStopFailures: String? = null,
            val Revision: DeploymentGroup.RevisionProperty
        ) 


        data class RevisionProperty(
            val GitHubLocation: DeploymentGroup.RevisionProperty.GitHubLocationProperty? = null,
            val RevisionType: String? = null,
            val S3Location: DeploymentGroup.RevisionProperty.S3LocationProperty? = null
        ) {

            data class GitHubLocationProperty(
                val CommitId: String,
                val Repository: String
            ) 


            data class S3LocationProperty(
                val Bucket: String,
                val BundleType: String,
                val ETag: String? = null,
                val Key: String,
                val Version: String? = null
            ) 

        }


        data class Ec2TagFilterProperty(
            val Key: String? = null,
            val Type: String,
            val Value: String? = null
        ) 


        data class OnPremisesInstanceTagFilterProperty(
            val Key: String? = null,
            val Type: String? = null,
            val Value: String? = null
        ) 

    }


}