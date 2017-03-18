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

}

@DslScope
class StepFunctionsContext(context: IacContext) : BaseStepFunctionsContext(context) {

}