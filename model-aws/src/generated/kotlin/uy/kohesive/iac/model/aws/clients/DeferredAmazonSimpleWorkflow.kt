package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.simpleworkflow.AbstractAmazonSimpleWorkflow
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow
import com.amazonaws.services.simpleworkflow.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonSimpleWorkflow(val context: IacContext) : AbstractAmazonSimpleWorkflow(), AmazonSimpleWorkflow {


}

class DeferredAmazonSimpleWorkflow(context: IacContext) : BaseDeferredAmazonSimpleWorkflow(context)
