package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.lightsail.AbstractAmazonLightsail
import com.amazonaws.services.lightsail.AmazonLightsail
import com.amazonaws.services.lightsail.model.CreateKeyPairRequest
import com.amazonaws.services.lightsail.model.CreateKeyPairResult
import com.amazonaws.services.lightsail.model.KeyPair
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonLightsail(val context: IacContext) : AbstractAmazonLightsail(), AmazonLightsail {

    override fun createKeyPair(request: CreateKeyPairRequest): CreateKeyPairResult {
        return with (context) {
            request.registerWithAutoName()
            CreateKeyPairResult().withKeyPair(
                makeProxy<CreateKeyPairRequest, KeyPair>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonLightsail(context: IacContext) : BaseDeferredAmazonLightsail(context)
