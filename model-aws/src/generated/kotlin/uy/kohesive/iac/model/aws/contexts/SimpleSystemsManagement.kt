package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface SimpleSystemsManagementIdentifiable : KohesiveIdentifiable {

}

interface SimpleSystemsManagementEnabled : SimpleSystemsManagementIdentifiable {
    val simpleSystemsManagementClient: AWSSimpleSystemsManagement
    val simpleSystemsManagementContext: SimpleSystemsManagementContext
    fun <T> withSimpleSystemsManagementContext(init: SimpleSystemsManagementContext.(AWSSimpleSystemsManagement) -> T): T = simpleSystemsManagementContext.init(simpleSystemsManagementClient)
}

open class BaseSimpleSystemsManagementContext(protected val context: IacContext) : SimpleSystemsManagementEnabled by context {

}

@DslScope
class SimpleSystemsManagementContext(context: IacContext) : BaseSimpleSystemsManagementContext(context) {

}