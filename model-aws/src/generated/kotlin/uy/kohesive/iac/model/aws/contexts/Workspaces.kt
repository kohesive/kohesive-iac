package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.workspaces.AmazonWorkspaces
import com.amazonaws.services.workspaces.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface WorkspacesIdentifiable : KohesiveIdentifiable {

}

interface WorkspacesEnabled : WorkspacesIdentifiable {
    val workspacesClient: AmazonWorkspaces
    val workspacesContext: WorkspacesContext
    fun <T> withWorkspacesContext(init: WorkspacesContext.(AmazonWorkspaces) -> T): T = workspacesContext.init(workspacesClient)
}

open class BaseWorkspacesContext(protected val context: IacContext) : WorkspacesEnabled by context {

}

@DslScope
class WorkspacesContext(context: IacContext) : BaseWorkspacesContext(context) {

}