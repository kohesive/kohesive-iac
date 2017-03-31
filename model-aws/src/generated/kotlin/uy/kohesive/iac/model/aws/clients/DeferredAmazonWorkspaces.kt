package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.workspaces.AbstractAmazonWorkspaces
import com.amazonaws.services.workspaces.AmazonWorkspaces
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonWorkspaces(val context: IacContext) : AbstractAmazonWorkspaces(), AmazonWorkspaces {


}

class DeferredAmazonWorkspaces(context: IacContext) : BaseDeferredAmazonWorkspaces(context)
