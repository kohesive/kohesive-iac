package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object KMS {

    @CloudFormationType("AWS::KMS::Alias")
    data class Alias(
        val AliasName: String,
        val TargetKeyId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::KMS::Key")
    data class Key(
        val Description: String? = null,
        val Enabled: String? = null,
        val EnableKeyRotation: String? = null,
        val KeyPolicy: Any
    ) : ResourceProperties 


}