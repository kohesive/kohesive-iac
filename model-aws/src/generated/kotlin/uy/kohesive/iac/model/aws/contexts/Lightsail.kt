package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.lightsail.AmazonLightsail
import com.amazonaws.services.lightsail.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface LightsailIdentifiable : KohesiveIdentifiable {

}

interface LightsailEnabled : LightsailIdentifiable {
    val lightsailClient: AmazonLightsail
    val lightsailContext: LightsailContext
    fun <T> withLightsailContext(init: LightsailContext.(AmazonLightsail) -> T): T = lightsailContext.init(lightsailClient)
}

open class BaseLightsailContext(protected val context: IacContext) : LightsailEnabled by context {

    open fun createDomain(domainName: String, init: CreateDomainRequest.() -> Unit): CreateDomainResult {
        return lightsailClient.createDomain(CreateDomainRequest().apply {
            withDomainName(domainName)
            init()
        })
    }

    open fun createInstanceSnapshot(instanceSnapshotName: String, init: CreateInstanceSnapshotRequest.() -> Unit): CreateInstanceSnapshotResult {
        return lightsailClient.createInstanceSnapshot(CreateInstanceSnapshotRequest().apply {
            withInstanceSnapshotName(instanceSnapshotName)
            init()
        })
    }

    open fun createKeyPair(keyPairName: String, init: CreateKeyPairRequest.() -> Unit): KeyPair {
        return lightsailClient.createKeyPair(CreateKeyPairRequest().apply {
            withKeyPairName(keyPairName)
            init()
        }).getKeyPair()
    }


}

@DslScope
class LightsailContext(context: IacContext) : BaseLightsailContext(context) {

}
