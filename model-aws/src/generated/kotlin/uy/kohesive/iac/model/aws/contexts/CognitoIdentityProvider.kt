package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CognitoIdentityProviderIdentifiable : KohesiveIdentifiable {

}

interface CognitoIdentityProviderEnabled : CognitoIdentityProviderIdentifiable {
    val cognitoIdentityProviderClient: AWSCognitoIdentityProvider
    val cognitoIdentityProviderContext: CognitoIdentityProviderContext
    fun <T> withCognitoIdentityProviderContext(init: CognitoIdentityProviderContext.(AWSCognitoIdentityProvider) -> T): T = cognitoIdentityProviderContext.init(cognitoIdentityProviderClient)
}

open class BaseCognitoIdentityProviderContext(protected val context: IacContext) : CognitoIdentityProviderEnabled by context {

}

@DslScope
class CognitoIdentityProviderContext(context: IacContext) : BaseCognitoIdentityProviderContext(context) {

}