package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.lambda.AWSLambda
import com.amazonaws.services.lambda.model.CreateAliasRequest
import com.amazonaws.services.lambda.model.CreateAliasResult
import com.amazonaws.services.lambda.model.CreateFunctionRequest
import com.amazonaws.services.lambda.model.CreateFunctionResult
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

    fun createAlias(name: String, init: CreateAliasRequest.() -> Unit): CreateAliasResult {
        return lambdaClient.createAlias(CreateAliasRequest().apply {
            withName(name)
            init()
        })
    }

    fun createFunction(functionName: String, init: CreateFunctionRequest.() -> Unit): CreateFunctionResult {
        return lambdaClient.createFunction(CreateFunctionRequest().apply {
            withFunctionName(functionName)
            init()
        })
    }


}

@DslScope
class LambdaContext(context: IacContext) : BaseLambdaContext(context) {

}
