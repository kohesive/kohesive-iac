package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cognitosync.AmazonCognitoSync
import com.amazonaws.services.cognitosync.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CognitoSyncIdentifiable : KohesiveIdentifiable {

}

interface CognitoSyncEnabled : CognitoSyncIdentifiable {
    val cognitoSyncClient: AmazonCognitoSync
    val cognitoSyncContext: CognitoSyncContext
    fun <T> withCognitoSyncContext(init: CognitoSyncContext.(AmazonCognitoSync) -> T): T = cognitoSyncContext.init(cognitoSyncClient)
}

open class BaseCognitoSyncContext(protected val context: IacContext) : CognitoSyncEnabled by context {


}

@DslScope
class CognitoSyncContext(context: IacContext) : BaseCognitoSyncContext(context) {

}
