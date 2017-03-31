package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.stepfunctions.AWSStepFunctions
import com.amazonaws.services.stepfunctions.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface StepFunctionsIdentifiable : KohesiveIdentifiable {

}

interface StepFunctionsEnabled : StepFunctionsIdentifiable {
    val stepFunctionsClient: AWSStepFunctions
    val stepFunctionsContext: StepFunctionsContext
    fun <T> withStepFunctionsContext(init: StepFunctionsContext.(AWSStepFunctions) -> T): T = stepFunctionsContext.init(stepFunctionsClient)
}

open class BaseStepFunctionsContext(protected val context: IacContext) : StepFunctionsEnabled by context {

    fun createActivity(name: String, init: CreateActivityRequest.() -> Unit): CreateActivityResult {
        return stepFunctionsClient.createActivity(CreateActivityRequest().apply {
            withName(name)
            init()
        })
    }

    fun createStateMachine(name: String, init: CreateStateMachineRequest.() -> Unit): CreateStateMachineResult {
        return stepFunctionsClient.createStateMachine(CreateStateMachineRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class StepFunctionsContext(context: IacContext) : BaseStepFunctionsContext(context) {

}
