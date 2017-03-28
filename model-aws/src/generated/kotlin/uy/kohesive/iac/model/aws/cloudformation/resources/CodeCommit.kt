package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object CodeCommit {

    @CloudFormationType("AWS::CodeCommit::Repository")
    data class Repository(
        val RepositoryDescription: String? = null,
        val RepositoryName: String,
        val Triggers: List<CodeCommit.Repository.TriggerProperty>? = null
    ) : ResourceProperties {

        data class TriggerProperty(
            val Branches: List<String>? = null,
            val CustomData: String? = null,
            val DestinationArn: String? = null,
            val Events: List<String>? = null,
            val Name: String? = null
        ) 

    }


}