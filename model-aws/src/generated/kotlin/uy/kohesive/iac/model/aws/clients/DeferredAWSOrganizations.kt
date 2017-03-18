package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.organizations.AbstractAWSOrganizations
import com.amazonaws.services.organizations.AWSOrganizations
import com.amazonaws.services.organizations.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSOrganizations(val context: IacContext) : AbstractAWSOrganizations(), AWSOrganizations {

}

class DeferredAWSOrganizations(context: IacContext) : BaseDeferredAWSOrganizations(context)