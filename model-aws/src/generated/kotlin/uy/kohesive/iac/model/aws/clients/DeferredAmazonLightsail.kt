package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.lightsail.AbstractAmazonLightsail
import com.amazonaws.services.lightsail.AmazonLightsail
import com.amazonaws.services.lightsail.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonLightsail(val context: IacContext) : AbstractAmazonLightsail(), AmazonLightsail {

    override fun attachStaticIp(request: AttachStaticIpRequest): AttachStaticIpResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AttachStaticIpRequest, AttachStaticIpResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

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

    override fun createDomainEntry(request: CreateDomainEntryRequest): CreateDomainEntryResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDomainEntryRequest, CreateDomainEntryResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createInstanceSnapshot(request: CreateInstanceSnapshotRequest): CreateInstanceSnapshotResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateInstanceSnapshotRequest, CreateInstanceSnapshotResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createInstances(request: CreateInstancesRequest): CreateInstancesResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateInstancesRequest, CreateInstancesResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createInstancesFromSnapshot(request: CreateInstancesFromSnapshotRequest): CreateInstancesFromSnapshotResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateInstancesFromSnapshotRequest, CreateInstancesFromSnapshotResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

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
