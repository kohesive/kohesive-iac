package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.apigateway.AmazonApiGateway
import com.amazonaws.services.apigateway.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ApiGatewayIdentifiable : KohesiveIdentifiable {

}

interface ApiGatewayEnabled : ApiGatewayIdentifiable {
    val apiGatewayClient: AmazonApiGateway
    val apiGatewayContext: ApiGatewayContext
    fun <T> withApiGatewayContext(init: ApiGatewayContext.(AmazonApiGateway) -> T): T = apiGatewayContext.init(apiGatewayClient)
}

open class BaseApiGatewayContext(protected val context: IacContext) : ApiGatewayEnabled by context {

    fun createApiKey(name: String, init: CreateApiKeyRequest.() -> Unit): CreateApiKeyResult {
        return apiGatewayClient.createApiKey(CreateApiKeyRequest().apply {
            withName(name)
            init()
        })
    }

    fun createAuthorizer(name: String, init: CreateAuthorizerRequest.() -> Unit): CreateAuthorizerResult {
        return apiGatewayClient.createAuthorizer(CreateAuthorizerRequest().apply {
            withName(name)
            init()
        })
    }

    fun createModel(name: String, init: CreateModelRequest.() -> Unit): CreateModelResult {
        return apiGatewayClient.createModel(CreateModelRequest().apply {
            withName(name)
            init()
        })
    }

    fun createRestApi(name: String, init: CreateRestApiRequest.() -> Unit): CreateRestApiResult {
        return apiGatewayClient.createRestApi(CreateRestApiRequest().apply {
            withName(name)
            init()
        })
    }

    fun createStage(stageName: String, init: CreateStageRequest.() -> Unit): CreateStageResult {
        return apiGatewayClient.createStage(CreateStageRequest().apply {
            withStageName(stageName)
            init()
        })
    }

    fun createUsagePlan(name: String, init: CreateUsagePlanRequest.() -> Unit): CreateUsagePlanResult {
        return apiGatewayClient.createUsagePlan(CreateUsagePlanRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class ApiGatewayContext(context: IacContext) : BaseApiGatewayContext(context) {

}
