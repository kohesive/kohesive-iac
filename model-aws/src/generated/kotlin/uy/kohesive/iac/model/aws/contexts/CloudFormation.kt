package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CloudFormationIdentifiable : KohesiveIdentifiable {

}

interface CloudFormationEnabled : CloudFormationIdentifiable {
    val cloudFormationClient: AmazonCloudFormation
    val cloudFormationContext: CloudFormationContext
    fun <T> withCloudFormationContext(init: CloudFormationContext.(AmazonCloudFormation) -> T): T = cloudFormationContext.init(cloudFormationClient)
}

open class BaseCloudFormationContext(protected val context: IacContext) : CloudFormationEnabled by context {

}

@DslScope
class CloudFormationContext(context: IacContext) : BaseCloudFormationContext(context) {

}