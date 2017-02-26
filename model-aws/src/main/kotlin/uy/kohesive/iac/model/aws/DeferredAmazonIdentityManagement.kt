package uy.kohesive.iac.model.aws

import com.amazonaws.services.identitymanagement.AbstractAmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement

class DeferredAmazonIdentityManagement(val context: IacContext) : AbstractAmazonIdentityManagement(), AmazonIdentityManagement {

}