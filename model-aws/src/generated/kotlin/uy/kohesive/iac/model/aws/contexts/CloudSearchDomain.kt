package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain
import com.amazonaws.services.cloudsearchdomain.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CloudSearchDomainIdentifiable : KohesiveIdentifiable {

}

interface CloudSearchDomainEnabled : CloudSearchDomainIdentifiable {
    val cloudSearchDomainClient: AmazonCloudSearchDomain
    val cloudSearchDomainContext: CloudSearchDomainContext
    fun <T> withCloudSearchDomainContext(init: CloudSearchDomainContext.(AmazonCloudSearchDomain) -> T): T = cloudSearchDomainContext.init(cloudSearchDomainClient)
}

open class BaseCloudSearchDomainContext(protected val context: IacContext) : CloudSearchDomainEnabled by context {

}

@DslScope
class CloudSearchDomainContext(context: IacContext) : BaseCloudSearchDomainContext(context) {

}