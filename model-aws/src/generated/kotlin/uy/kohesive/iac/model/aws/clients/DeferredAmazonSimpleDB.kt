package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.simpledb.AbstractAmazonSimpleDB
import com.amazonaws.services.simpledb.AmazonSimpleDB
import com.amazonaws.services.simpledb.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonSimpleDB(val context: IacContext) : AbstractAmazonSimpleDB(), AmazonSimpleDB {

    override fun createDomain(request: CreateDomainRequest): CreateDomainResult {
        return with (context) {
            request.registerWithAutoName()
            CreateDomainResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonSimpleDB(context: IacContext) : BaseDeferredAmazonSimpleDB(context)
