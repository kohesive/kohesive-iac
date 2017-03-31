package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudfront.AbstractAmazonCloudFront
import com.amazonaws.services.cloudfront.AmazonCloudFront
import com.amazonaws.services.cloudfront.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonCloudFront(val context: IacContext) : AbstractAmazonCloudFront(), AmazonCloudFront {

    override fun createCloudFrontOriginAccessIdentity(request: CreateCloudFrontOriginAccessIdentityRequest): CreateCloudFrontOriginAccessIdentityResult {
        return with (context) {
            request.registerWithAutoName()
            CreateCloudFrontOriginAccessIdentityResult().withCloudFrontOriginAccessIdentity(
                makeProxy<CreateCloudFrontOriginAccessIdentityRequest, CloudFrontOriginAccessIdentity>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateCloudFrontOriginAccessIdentityRequest::getCloudFrontOriginAccessIdentityConfig to CloudFrontOriginAccessIdentity::getCloudFrontOriginAccessIdentityConfig
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createDistribution(request: CreateDistributionRequest): CreateDistributionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateDistributionResult().withDistribution(
                makeProxy<CreateDistributionRequest, Distribution>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateDistributionRequest::getDistributionConfig to Distribution::getDistributionConfig
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createDistributionWithTags(request: CreateDistributionWithTagsRequest): CreateDistributionWithTagsResult {
        return with (context) {
            request.registerWithAutoName()
            CreateDistributionWithTagsResult().withDistribution(
                makeProxy<CreateDistributionWithTagsRequest, Distribution>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createInvalidation(request: CreateInvalidationRequest): CreateInvalidationResult {
        return with (context) {
            request.registerWithAutoName()
            CreateInvalidationResult().withInvalidation(
                makeProxy<CreateInvalidationRequest, Invalidation>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateInvalidationRequest::getInvalidationBatch to Invalidation::getInvalidationBatch
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createStreamingDistribution(request: CreateStreamingDistributionRequest): CreateStreamingDistributionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateStreamingDistributionResult().withStreamingDistribution(
                makeProxy<CreateStreamingDistributionRequest, StreamingDistribution>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateStreamingDistributionRequest::getStreamingDistributionConfig to StreamingDistribution::getStreamingDistributionConfig
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createStreamingDistributionWithTags(request: CreateStreamingDistributionWithTagsRequest): CreateStreamingDistributionWithTagsResult {
        return with (context) {
            request.registerWithAutoName()
            CreateStreamingDistributionWithTagsResult().withStreamingDistribution(
                makeProxy<CreateStreamingDistributionWithTagsRequest, StreamingDistribution>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonCloudFront(context: IacContext) : BaseDeferredAmazonCloudFront(context)
