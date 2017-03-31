package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.kms.AbstractAWSKMS
import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSKMS(val context: IacContext) : AbstractAWSKMS(), AWSKMS {

    override fun createAlias(request: CreateAliasRequest): CreateAliasResult {
        return with (context) {
            request.registerWithAutoName()
            CreateAliasResult().registerWithSameNameAs(request)
        }
    }

    override fun createGrant(request: CreateGrantRequest): CreateGrantResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateGrantRequest, CreateGrantResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createKey(request: CreateKeyRequest): CreateKeyResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateKeyRequest, CreateKeyResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSKMS(context: IacContext) : BaseDeferredAWSKMS(context)
