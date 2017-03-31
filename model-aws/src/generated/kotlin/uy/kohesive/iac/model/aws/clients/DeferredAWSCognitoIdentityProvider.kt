package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cognitoidp.AbstractAWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCognitoIdentityProvider(val context: IacContext) : AbstractAWSCognitoIdentityProvider(), AWSCognitoIdentityProvider {

    override fun addCustomAttributes(request: AddCustomAttributesRequest): AddCustomAttributesResult {
        return with (context) {
            request.registerWithAutoName()
            AddCustomAttributesResult().registerWithSameNameAs(request)
        }
    }

    override fun createGroup(request: CreateGroupRequest): CreateGroupResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateGroupRequest, CreateGroupResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createUserImportJob(request: CreateUserImportJobRequest): CreateUserImportJobResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateUserImportJobRequest, CreateUserImportJobResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createUserPool(request: CreateUserPoolRequest): CreateUserPoolResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateUserPoolRequest, CreateUserPoolResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createUserPoolClient(request: CreateUserPoolClientRequest): CreateUserPoolClientResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateUserPoolClientRequest, CreateUserPoolClientResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSCognitoIdentityProvider(context: IacContext) : BaseDeferredAWSCognitoIdentityProvider(context)
