package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudsearchv2.AbstractAmazonCloudSearch
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearch
import com.amazonaws.services.cloudsearchv2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonCloudSearch(val context: IacContext) : AbstractAmazonCloudSearch(), AmazonCloudSearch {

    override fun createDomain(request: CreateDomainRequest): CreateDomainResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDomainRequest, CreateDomainResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonCloudSearch(context: IacContext) : BaseDeferredAmazonCloudSearch(context)
