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

    fun createAssociation(name: String, init: CreateAssociationRequest.() -> Unit): AssociationDescription {
        return simpleSystemsManagementClient.createAssociation(CreateAssociationRequest().apply {
            withName(name)
            init()
        }).associationDescription
    }

    fun createDocument(name: String, init: CreateDocumentRequest.() -> Unit): DocumentDescription {
        return simpleSystemsManagementClient.createDocument(CreateDocumentRequest().apply {
            withName(name)
            init()
        }).documentDescription
    }

    fun createMaintenanceWindow(name: String, init: CreateMaintenanceWindowRequest.() -> Unit): CreateMaintenanceWindowResult {
        return simpleSystemsManagementClient.createMaintenanceWindow(CreateMaintenanceWindowRequest().apply {
            withName(name)
            init()
        })
    }

    fun createPatchBaseline(name: String, init: CreatePatchBaselineRequest.() -> Unit): CreatePatchBaselineResult {
        return simpleSystemsManagementClient.createPatchBaseline(CreatePatchBaselineRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class SimpleSystemsManagementContext(context: IacContext) : BaseSimpleSystemsManagementContext(context) {

}
