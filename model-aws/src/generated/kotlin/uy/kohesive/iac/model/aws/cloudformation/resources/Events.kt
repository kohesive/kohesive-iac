package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object Events {

    @CloudFormationType("AWS::Events::Rule")
    data class Rule(
        val Description: String? = null,
        val EventPattern: Any? = null,
        val Name: String? = null,
        val RoleArn: String? = null,
        val ScheduleExpression: String? = null,
        val State: String? = null,
        val Targets: List<Events.Rule.TargetProperty>? = null
    ) : ResourceProperties {

        data class TargetProperty(
            val Arn: String,
            val Id: String,
            val Input: String? = null,
            val InputPath: String? = null
        ) 

    }


}