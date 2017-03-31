package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.servicecatalog.AbstractAWSServiceCatalog
import com.amazonaws.services.servicecatalog.AWSServiceCatalog
import com.amazonaws.services.servicecatalog.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSServiceCatalog(val context: IacContext) : AbstractAWSServiceCatalog(), AWSServiceCatalog {

    override fun createConstraint(request: CreateConstraintRequest): CreateConstraintResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateConstraintRequest, CreateConstraintResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createPortfolio(request: CreatePortfolioRequest): CreatePortfolioResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePortfolioRequest, CreatePortfolioResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreatePortfolioRequest::getTags to CreatePortfolioResult::getTags
                )
            )
        }
    }

    override fun createPortfolioShare(request: CreatePortfolioShareRequest): CreatePortfolioShareResult {
        return with (context) {
            request.registerWithAutoName()
            CreatePortfolioShareResult().registerWithSameNameAs(request)
        }
    }

    override fun createProduct(request: CreateProductRequest): CreateProductResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateProductRequest, CreateProductResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateProductRequest::getTags to CreateProductResult::getTags
                )
            )
        }
    }

    override fun createProvisioningArtifact(request: CreateProvisioningArtifactRequest): CreateProvisioningArtifactResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateProvisioningArtifactRequest, CreateProvisioningArtifactResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSServiceCatalog(context: IacContext) : BaseDeferredAWSServiceCatalog(context)
