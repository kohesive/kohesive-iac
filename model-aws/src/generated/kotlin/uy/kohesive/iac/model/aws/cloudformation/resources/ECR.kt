package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object ECR {

    @CloudFormationType("AWS::ECR::Repository")
    data class Repository(
        val RepositoryName: String? = null,
        val RepositoryPolicyText: Any? = null
    ) : ResourceProperties 


}