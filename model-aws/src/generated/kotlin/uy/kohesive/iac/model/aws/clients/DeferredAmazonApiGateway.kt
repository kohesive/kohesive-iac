package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.apigateway.AbstractAmazonApiGateway
import com.amazonaws.services.apigateway.AmazonApiGateway
import com.amazonaws.services.apigateway.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonApiGateway(val context: IacContext) : AbstractAmazonApiGateway(), AmazonApiGateway {


}

class DeferredAmazonApiGateway(context: IacContext) : BaseDeferredAmazonApiGateway(context)
