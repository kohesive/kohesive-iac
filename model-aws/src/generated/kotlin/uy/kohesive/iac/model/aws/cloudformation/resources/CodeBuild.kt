package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object CodeBuild {

    @CloudFormationType("AWS::CodeBuild::Project")
    data class Project(
        val Artifacts: Project.ArtifactProperty,
        val Description: String? = null,
        val EncryptionKey: String? = null,
        val Environment: Project.EnvironmentProperty,
        val Name: String,
        val ServiceRole: String,
        val Source: Project.SourceProperty,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val TimeoutInMinutes: String? = null
    ) : ResourceProperties {

        data class ArtifactProperty(
            val Location: String? = null,
            val Name: String? = null,
            val NamespaceType: String? = null,
            val Packaging: String? = null,
            val Path: String? = null,
            val Type: String
        ) 


        data class EnvironmentProperty(
            val ComputeType: String,
            val EnvironmentVariables: List<CodeBuild.Project.EnvironmentProperty.EnvironmentVariableProperty>? = null,
            val Image: String,
            val Type: String
        ) {

            data class EnvironmentVariableProperty(
                val Name: String,
                val Value: String
            ) 

        }


        data class SourceProperty(
            val BuildSpec: String? = null,
            val Location: String? = null,
            val Type: String
        ) 

    }


}