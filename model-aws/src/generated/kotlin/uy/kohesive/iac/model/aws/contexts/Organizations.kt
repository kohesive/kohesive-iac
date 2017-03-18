package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.organizations.AWSOrganizations
import com.amazonaws.services.organizations.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface OrganizationsIdentifiable : KohesiveIdentifiable {

}

interface OrganizationsEnabled : OrganizationsIdentifiable {
    val organizationsClient: AWSOrganizations
    val organizationsContext: OrganizationsContext
    fun <T> withOrganizationsContext(init: OrganizationsContext.(AWSOrganizations) -> T): T = organizationsContext.init(organizationsClient)
}

open class BaseOrganizationsContext(protected val context: IacContext) : OrganizationsEnabled by context {

}

@DslScope
class OrganizationsContext(context: IacContext) : BaseOrganizationsContext(context) {

}