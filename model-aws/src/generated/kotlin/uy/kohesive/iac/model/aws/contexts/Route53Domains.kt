package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.route53domains.AmazonRoute53Domains
import com.amazonaws.services.route53domains.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface Route53DomainsIdentifiable : KohesiveIdentifiable {

}

interface Route53DomainsEnabled : Route53DomainsIdentifiable {
    val route53DomainsClient: AmazonRoute53Domains
    val route53DomainsContext: Route53DomainsContext
    fun <T> withRoute53DomainsContext(init: Route53DomainsContext.(AmazonRoute53Domains) -> T): T = route53DomainsContext.init(route53DomainsClient)
}

open class BaseRoute53DomainsContext(protected val context: IacContext) : Route53DomainsEnabled by context {


}

@DslScope
class Route53DomainsContext(context: IacContext) : BaseRoute53DomainsContext(context) {

}
