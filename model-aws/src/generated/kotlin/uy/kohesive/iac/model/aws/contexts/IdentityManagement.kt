package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface IdentityManagementIdentifiable : KohesiveIdentifiable {

}

interface IdentityManagementEnabled : IdentityManagementIdentifiable {
    val identityManagementClient: AmazonIdentityManagement
    val identityManagementContext: IdentityManagementContext
    fun <T> withIdentityManagementContext(init: IdentityManagementContext.(AmazonIdentityManagement) -> T): T = identityManagementContext.init(identityManagementClient)
}

open class BaseIdentityManagementContext(protected val context: IacContext) : IdentityManagementEnabled by context {

}
