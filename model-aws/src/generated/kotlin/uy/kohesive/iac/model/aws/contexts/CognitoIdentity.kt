package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity
import com.amazonaws.services.cognitoidentity.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CognitoIdentityIdentifiable : KohesiveIdentifiable {

}

interface CognitoIdentityEnabled : CognitoIdentityIdentifiable {
    val cognitoIdentityClient: AmazonCognitoIdentity
    val cognitoIdentityContext: CognitoIdentityContext
    fun <T> withCognitoIdentityContext(init: CognitoIdentityContext.(AmazonCognitoIdentity) -> T): T = cognitoIdentityContext.init(cognitoIdentityClient)
}

open class BaseCognitoIdentityContext(protected val context: IacContext) : CognitoIdentityEnabled by context {

    open fun createIdentityPool(identityPoolName: String, init: CreateIdentityPoolRequest.() -> Unit): CreateIdentityPoolResult {
        return cognitoIdentityClient.createIdentityPool(CreateIdentityPoolRequest().apply {
            withIdentityPoolName(identityPoolName)
            init()
        })
    }


}

@DslScope
class CognitoIdentityContext(context: IacContext) : BaseCognitoIdentityContext(context) {

}
