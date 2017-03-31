package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudformation.AbstractAmazonCloudFormation
import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonCloudFormation(val context: IacContext) : AbstractAmazonCloudFormation(), AmazonCloudFormation {

    override fun createChangeSet(request: CreateChangeSetRequest): CreateChangeSetResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateChangeSetRequest, CreateChangeSetResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createStack(request: CreateStackRequest): CreateStackResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateStackRequest, CreateStackResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonCloudFormation(context: IacContext) : BaseDeferredAmazonCloudFormation(context)
