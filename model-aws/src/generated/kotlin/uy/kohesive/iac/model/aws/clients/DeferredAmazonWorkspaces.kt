package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.workspaces.AbstractAmazonWorkspaces
import com.amazonaws.services.workspaces.AmazonWorkspaces
import com.amazonaws.services.workspaces.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonWorkspaces(val context: IacContext) : AbstractAmazonWorkspaces(), AmazonWorkspaces {

    override fun createTags(request: CreateTagsRequest): CreateTagsResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTagsResult().registerWithSameNameAs(request)
        }
    }

    override fun createWorkspaces(request: CreateWorkspacesRequest): CreateWorkspacesResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateWorkspacesRequest, CreateWorkspacesResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonWorkspaces(context: IacContext) : BaseDeferredAmazonWorkspaces(context)
