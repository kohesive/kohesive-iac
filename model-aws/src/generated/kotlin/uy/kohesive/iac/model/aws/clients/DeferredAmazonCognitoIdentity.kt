package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cognitoidentity.AbstractAmazonCognitoIdentity
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity
import com.amazonaws.services.cognitoidentity.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonCognitoIdentity(val context: IacContext) : AbstractAmazonCognitoIdentity(), AmazonCognitoIdentity {

}

class DeferredAmazonCognitoIdentity(context: IacContext) : BaseDeferredAmazonCognitoIdentity(context)