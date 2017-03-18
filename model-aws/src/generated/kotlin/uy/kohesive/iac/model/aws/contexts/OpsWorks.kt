package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.opsworks.AWSOpsWorks
import com.amazonaws.services.opsworks.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface OpsWorksIdentifiable : KohesiveIdentifiable {

}

interface OpsWorksEnabled : OpsWorksIdentifiable {
    val opsWorksClient: AWSOpsWorks
    val opsWorksContext: OpsWorksContext
    fun <T> withOpsWorksContext(init: OpsWorksContext.(AWSOpsWorks) -> T): T = opsWorksContext.init(opsWorksClient)
}

open class BaseOpsWorksContext(protected val context: IacContext) : OpsWorksEnabled by context {

}

@DslScope
class OpsWorksContext(context: IacContext) : BaseOpsWorksContext(context) {

}