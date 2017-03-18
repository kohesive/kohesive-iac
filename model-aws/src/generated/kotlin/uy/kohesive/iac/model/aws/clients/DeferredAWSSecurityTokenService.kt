package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.securitytoken.AbstractAWSSecurityTokenService
import com.amazonaws.services.securitytoken.AWSSecurityTokenService
import com.amazonaws.services.securitytoken.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSSecurityTokenService(val context: IacContext) : AbstractAWSSecurityTokenService(), AWSSecurityTokenService {

}

class DeferredAWSSecurityTokenService(context: IacContext) : BaseDeferredAWSSecurityTokenService(context)