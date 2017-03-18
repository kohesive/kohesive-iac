package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.lambda.AWSLambda
import com.amazonaws.services.lambda.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface LambdaIdentifiable : KohesiveIdentifiable {

}

interface LambdaEnabled : LambdaIdentifiable {
    val lambdaClient: AWSLambda
    val lambdaContext: LambdaContext
    fun <T> withLambdaContext(init: LambdaContext.(AWSLambda) -> T): T = lambdaContext.init(lambdaClient)
}

open class BaseLambdaContext(protected val context: IacContext) : LambdaEnabled by context {

}

@DslScope
class LambdaContext(context: IacContext) : BaseLambdaContext(context) {

}