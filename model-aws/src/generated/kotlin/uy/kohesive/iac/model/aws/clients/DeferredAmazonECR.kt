package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.ecr.AbstractAmazonECR
import com.amazonaws.services.ecr.AmazonECR
import com.amazonaws.services.ecr.model.CreateRepositoryRequest
import com.amazonaws.services.ecr.model.CreateRepositoryResult
import com.amazonaws.services.ecr.model.Repository
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonECR(val context: IacContext) : AbstractAmazonECR(), AmazonECR {

    override fun createRepository(request: CreateRepositoryRequest): CreateRepositoryResult {
        return with (context) {
            request.registerWithAutoName()
            CreateRepositoryResult().withRepository(
                makeProxy<CreateRepositoryRequest, Repository>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateRepositoryRequest::getRepositoryName to Repository::getRepositoryName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonECR(context: IacContext) : BaseDeferredAmazonECR(context)
