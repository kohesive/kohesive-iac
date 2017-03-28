package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object SSM {

    @CloudFormationType("AWS::SSM::Association")
    data class Association(
        val DocumentVersion: String? = null,
        val InstanceId: String? = null,
        val Name: String,
        val Parameters: Map<String, List<String>>? = null,
        val ScheduleExpression: String? = null,
        val Targets: List<SSM.Association.TargetProperty>? = null
    ) : ResourceProperties {

        data class TargetProperty(
            val Key: String,
            val Values: List<String>
        ) 

    }

    @CloudFormationType("AWS::SSM::Document")
    data class Document(
        val Content: Any,
        val DocumentType: String? = null
    ) : ResourceProperties 


}