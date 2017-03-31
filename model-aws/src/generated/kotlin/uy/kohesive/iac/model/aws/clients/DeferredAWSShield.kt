package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.shield.AbstractAWSShield
import com.amazonaws.services.shield.AWSShield
import com.amazonaws.services.shield.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSShield(val context: IacContext) : AbstractAWSShield(), AWSShield {

    override fun createProtection(request: CreateProtectionRequest): CreateProtectionResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateProtectionRequest, CreateProtectionResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createSubscription(request: CreateSubscriptionRequest): CreateSubscriptionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateSubscriptionResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSShield(context: IacContext) : BaseDeferredAWSShield(context)
