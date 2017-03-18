package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.identitymanagement.AbstractAmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonIdentityManagement(val context: IacContext) : AbstractAmazonIdentityManagement(), AmazonIdentityManagement {

}
