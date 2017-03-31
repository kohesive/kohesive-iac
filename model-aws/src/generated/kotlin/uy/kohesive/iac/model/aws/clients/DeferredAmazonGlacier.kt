package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.glacier.AbstractAmazonGlacier
import com.amazonaws.services.glacier.AmazonGlacier
import com.amazonaws.services.glacier.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonGlacier(val context: IacContext) : AbstractAmazonGlacier(), AmazonGlacier {

    override fun addTagsToVault(request: AddTagsToVaultRequest): AddTagsToVaultResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsToVaultResult().registerWithSameNameAs(request)
        }
    }

    override fun createVault(request: CreateVaultRequest): CreateVaultResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateVaultRequest, CreateVaultResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonGlacier(context: IacContext) : BaseDeferredAmazonGlacier(context)
