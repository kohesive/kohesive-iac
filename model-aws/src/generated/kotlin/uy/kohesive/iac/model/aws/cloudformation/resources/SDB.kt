package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object SDB {

    @CloudFormationType("AWS::SDB::Domain")
    data class Domain(
        val Description: String? = null
    ) : ResourceProperties 


}