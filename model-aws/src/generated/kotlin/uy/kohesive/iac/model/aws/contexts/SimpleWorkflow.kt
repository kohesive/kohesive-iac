package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow
import com.amazonaws.services.simpleworkflow.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface SimpleWorkflowIdentifiable : KohesiveIdentifiable {

}

interface SimpleWorkflowEnabled : SimpleWorkflowIdentifiable {
    val simpleWorkflowClient: AmazonSimpleWorkflow
    val simpleWorkflowContext: SimpleWorkflowContext
    fun <T> withSimpleWorkflowContext(init: SimpleWorkflowContext.(AmazonSimpleWorkflow) -> T): T = simpleWorkflowContext.init(simpleWorkflowClient)
}

open class BaseSimpleWorkflowContext(protected val context: IacContext) : SimpleWorkflowEnabled by context {

}

@DslScope
class SimpleWorkflowContext(context: IacContext) : BaseSimpleWorkflowContext(context) {

}