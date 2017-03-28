package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object Kinesis {

    @CloudFormationType("AWS::Kinesis::Stream")
    data class Stream(
        val Name: String? = null,
        val ShardCount: String,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 


}