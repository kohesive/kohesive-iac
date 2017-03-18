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

}

@DslScope
class ApiGatewayContext(context: IacContext) : BaseApiGatewayContext(context) {

}