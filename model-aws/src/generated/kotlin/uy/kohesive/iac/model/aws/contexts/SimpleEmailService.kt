package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface SimpleEmailServiceIdentifiable : KohesiveIdentifiable {

}

interface SimpleEmailServiceEnabled : SimpleEmailServiceIdentifiable {
    val simpleEmailServiceClient: AmazonSimpleEmailService
    val simpleEmailServiceContext: SimpleEmailServiceContext
    fun <T> withSimpleEmailServiceContext(init: SimpleEmailServiceContext.(AmazonSimpleEmailService) -> T): T = simpleEmailServiceContext.init(simpleEmailServiceClient)
}

open class BaseSimpleEmailServiceContext(protected val context: IacContext) : SimpleEmailServiceEnabled by context {

}

@DslScope
class SimpleEmailServiceContext(context: IacContext) : BaseSimpleEmailServiceContext(context) {

}