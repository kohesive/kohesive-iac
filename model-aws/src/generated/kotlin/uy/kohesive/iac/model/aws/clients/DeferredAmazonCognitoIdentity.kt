package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cognitoidentity.AbstractAmazonCognitoIdentity
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity
import com.amazonaws.services.cognitoidentity.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonCognitoIdentity(val context: IacContext) : AbstractAmazonCognitoIdentity(), AmazonCognitoIdentity {

    override fun createIdentityPool(request: CreateIdentityPoolRequest): CreateIdentityPoolResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateIdentityPoolRequest, CreateIdentityPoolResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateIdentityPoolRequest::getIdentityPoolName to CreateIdentityPoolResult::getIdentityPoolName,
                    CreateIdentityPoolRequest::getAllowUnauthenticatedIdentities to CreateIdentityPoolResult::getAllowUnauthenticatedIdentities,
                    CreateIdentityPoolRequest::getSupportedLoginProviders to CreateIdentityPoolResult::getSupportedLoginProviders,
                    CreateIdentityPoolRequest::getDeveloperProviderName to CreateIdentityPoolResult::getDeveloperProviderName,
                    CreateIdentityPoolRequest::getOpenIdConnectProviderARNs to CreateIdentityPoolResult::getOpenIdConnectProviderARNs,
                    CreateIdentityPoolRequest::getCognitoIdentityProviders to CreateIdentityPoolResult::getCognitoIdentityProviders,
                    CreateIdentityPoolRequest::getSamlProviderARNs to CreateIdentityPoolResult::getSamlProviderARNs
                )
            )
        }
    }


}

class DeferredAmazonCognitoIdentity(context: IacContext) : BaseDeferredAmazonCognitoIdentity(context)
