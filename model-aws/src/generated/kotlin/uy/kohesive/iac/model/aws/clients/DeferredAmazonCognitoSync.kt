package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cognitosync.AbstractAmazonCognitoSync
import com.amazonaws.services.cognitosync.AmazonCognitoSync
import com.amazonaws.services.cognitosync.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonCognitoSync(val context: IacContext) : AbstractAmazonCognitoSync(), AmazonCognitoSync {

}

class DeferredAmazonCognitoSync(context: IacContext) : BaseDeferredAmazonCognitoSync(context)