package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.securitytoken.AWSSecurityTokenService
import com.amazonaws.services.securitytoken.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface SecurityTokenServiceIdentifiable : KohesiveIdentifiable {

}

interface SecurityTokenServiceEnabled : SecurityTokenServiceIdentifiable {
    val securityTokenServiceClient: AWSSecurityTokenService
    val securityTokenServiceContext: SecurityTokenServiceContext
    fun <T> withSecurityTokenServiceContext(init: SecurityTokenServiceContext.(AWSSecurityTokenService) -> T): T = securityTokenServiceContext.init(securityTokenServiceClient)
}

open class BaseSecurityTokenServiceContext(protected val context: IacContext) : SecurityTokenServiceEnabled by context {

}

@DslScope
class SecurityTokenServiceContext(context: IacContext) : BaseSecurityTokenServiceContext(context) {

}