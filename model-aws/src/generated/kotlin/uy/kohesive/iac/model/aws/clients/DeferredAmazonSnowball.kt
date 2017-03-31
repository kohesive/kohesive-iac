package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.snowball.AbstractAmazonSnowball
import com.amazonaws.services.snowball.AmazonSnowball
import com.amazonaws.services.snowball.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonSnowball(val context: IacContext) : AbstractAmazonSnowball(), AmazonSnowball {

    override fun createAddress(request: CreateAddressRequest): CreateAddressResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateAddressRequest, CreateAddressResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createCluster(request: CreateClusterRequest): CreateClusterResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateClusterRequest, CreateClusterResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createJob(request: CreateJobRequest): CreateJobResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateJobRequest, CreateJobResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonSnowball(context: IacContext) : BaseDeferredAmazonSnowball(context)
