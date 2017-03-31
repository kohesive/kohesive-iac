package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.lambda.AbstractAWSLambda
import com.amazonaws.services.lambda.AWSLambda
import com.amazonaws.services.lambda.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSLambda(val context: IacContext) : AbstractAWSLambda(), AWSLambda {


}

class DeferredAWSLambda(context: IacContext) : BaseDeferredAWSLambda(context)
