package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudformation.AbstractAmazonCloudFormation
import com.amazonaws.services.cloudformation.AmazonCloudFormation
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonCloudFormation(val context: IacContext) : AbstractAmazonCloudFormation(), AmazonCloudFormation {


}

class DeferredAmazonCloudFormation(context: IacContext) : BaseDeferredAmazonCloudFormation(context)
