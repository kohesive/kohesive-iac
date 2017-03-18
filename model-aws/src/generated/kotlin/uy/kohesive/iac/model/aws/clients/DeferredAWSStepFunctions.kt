package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.stepfunctions.AbstractAWSStepFunctions
import com.amazonaws.services.stepfunctions.AWSStepFunctions
import com.amazonaws.services.stepfunctions.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSStepFunctions(val context: IacContext) : AbstractAWSStepFunctions(), AWSStepFunctions {

}

class DeferredAWSStepFunctions(context: IacContext) : BaseDeferredAWSStepFunctions(context)