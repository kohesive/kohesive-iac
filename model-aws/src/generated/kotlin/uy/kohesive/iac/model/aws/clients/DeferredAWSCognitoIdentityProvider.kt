package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cognitoidp.AbstractAWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCognitoIdentityProvider(val context: IacContext) : AbstractAWSCognitoIdentityProvider(), AWSCognitoIdentityProvider {


}

class DeferredAWSCognitoIdentityProvider(context: IacContext) : BaseDeferredAWSCognitoIdentityProvider(context)
