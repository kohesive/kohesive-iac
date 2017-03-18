package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.route53.AmazonRoute53
import com.amazonaws.services.route53.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface Route53Identifiable : KohesiveIdentifiable {

}

interface Route53Enabled : Route53Identifiable {
    val route53Client: AmazonRoute53
    val route53Context: Route53Context
    fun <T> withRoute53Context(init: Route53Context.(AmazonRoute53) -> T): T = route53Context.init(route53Client)
}

open class BaseRoute53Context(protected val context: IacContext) : Route53Enabled by context {

}

@DslScope
class Route53Context(context: IacContext) : BaseRoute53Context(context) {

}