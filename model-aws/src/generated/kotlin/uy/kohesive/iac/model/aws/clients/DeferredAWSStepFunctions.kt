package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.stepfunctions.AbstractAWSStepFunctions
import com.amazonaws.services.stepfunctions.AWSStepFunctions
import com.amazonaws.services.stepfunctions.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSStepFunctions(val context: IacContext) : AbstractAWSStepFunctions(), AWSStepFunctions {

    override fun createActivity(request: CreateActivityRequest): CreateActivityResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateActivityRequest, CreateActivityResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createStateMachine(request: CreateStateMachineRequest): CreateStateMachineResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateStateMachineRequest, CreateStateMachineResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSStepFunctions(context: IacContext) : BaseDeferredAWSStepFunctions(context)
