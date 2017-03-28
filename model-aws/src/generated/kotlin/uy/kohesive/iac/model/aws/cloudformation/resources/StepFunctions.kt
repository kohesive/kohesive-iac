package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object StepFunctions {

    @CloudFormationType("AWS::StepFunctions::Activity")
    data class Activity(
        val Name: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::StepFunctions::StateMachine")
    data class StateMachine(
        val DefinitionString: String,
        val RoleArn: String
    ) : ResourceProperties 


}