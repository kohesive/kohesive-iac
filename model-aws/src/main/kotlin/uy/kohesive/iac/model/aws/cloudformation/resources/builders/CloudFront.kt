package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.cloudfront.model.CreateDistributionRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFront

class CloudFrontDistributionResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDistributionRequest> {

    override val requestClazz = CreateDistributionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDistributionRequest).let {
            CloudFront.Distribution(
                DistributionConfig = request.distributionConfig.let {
                    CloudFront.Distribution.DistributionConfigProperty(
                        Aliases        = it.aliases?.items,
                        CacheBehaviors = it.cacheBehaviors?.items?.map {
                            CloudFront.Distribution.DistributionConfigProperty.CacheBehaviorProperty(
                                AllowedMethods  = it.allowedMethods?.items,
                                CachedMethods   = it.allowedMethods?.cachedMethods?.items,
                                Compress        = it.compress?.toString(),
                                DefaultTTL      = it.defaultTTL?.toString(),
                                ForwardedValues = it.forwardedValues.let {
                                    CloudFront.Distribution.DistributionConfigProperty.DefaultCacheBehaviorProperty.ForwardedValueProperty(
                                        Cookies = it.cookies?.let {
                                            CloudFront.Distribution.DistributionConfigProperty.DefaultCacheBehaviorProperty.ForwardedValueProperty.CookieProperty(
                                                Forward          = it.forward,
                                                WhitelistedNames = it.whitelistedNames?.items
                                            )
                                        },
                                        Headers     = it.headers?.items,
                                        QueryString = it.queryString.toString(),
                                        QueryStringCacheKeys = it.queryStringCacheKeys?.items
                                    )
                                },
                                MinTTL               = it.minTTL?.toString(),
                                MaxTTL               = it.maxTTL?.toString(),
                                PathPattern          = it.pathPattern,
                                SmoothStreaming      = it.smoothStreaming?.toString(),
                                TargetOriginId       = it.targetOriginId,
                                TrustedSigners       = it.trustedSigners?.items,
                                ViewerProtocolPolicy = it.viewerProtocolPolicy
                            )
                        },
                        Comment = it.comment,
                        CustomErrorResponses = it.customErrorResponses?.items?.map {
                            CloudFront.Distribution.DistributionConfigProperty.CustomErrorResponseProperty(
                                ErrorCachingMinTTL = it.errorCachingMinTTL?.toString(),
                                ErrorCode          = it.errorCode.toString(),
                                ResponseCode       = it.responseCode,
                                ResponsePagePath   = it.responsePagePath
                            )
                        },
                        DefaultCacheBehavior = it.defaultCacheBehavior.let {
                            CloudFront.Distribution.DistributionConfigProperty.DefaultCacheBehaviorProperty(
                                AllowedMethods  = it.allowedMethods?.items,
                                CachedMethods   = it.allowedMethods?.cachedMethods?.items,
                                Compress        = it.compress?.toString(),
                                DefaultTTL      = it.defaultTTL?.toString(),
                                ForwardedValues = it.forwardedValues.let {
                                    CloudFront.Distribution.DistributionConfigProperty.DefaultCacheBehaviorProperty.ForwardedValueProperty(
                                        Cookies = it.cookies?.let {
                                            CloudFront.Distribution.DistributionConfigProperty.DefaultCacheBehaviorProperty.ForwardedValueProperty.CookieProperty(
                                                Forward          = it.forward,
                                                WhitelistedNames = it.whitelistedNames?.items
                                            )
                                        },
                                        Headers     = it.headers?.items,
                                        QueryString = it.queryString.toString(),
                                        QueryStringCacheKeys = it.queryStringCacheKeys?.items
                                    )
                                },
                                MinTTL               = it.minTTL?.toString(),
                                MaxTTL               = it.maxTTL?.toString(),
                                SmoothStreaming      = it.smoothStreaming?.toString(),
                                TargetOriginId       = it.targetOriginId,
                                TrustedSigners       = it.trustedSigners?.items,
                                ViewerProtocolPolicy = it.viewerProtocolPolicy
                            )
                        },
                        DefaultRootObject = it.defaultRootObject,
                        Enabled           = it.enabled.toString(),
                        HttpVersion       = it.httpVersion,
                        Logging           = it.logging?.let {
                            CloudFront.Distribution.DistributionConfigProperty.LoggingProperty(
                                Bucket         = it.bucket,
                                IncludeCookies = it.includeCookies?.toString(),
                                Prefix         = it.prefix
                            )
                        },
                        Origins = it.origins.items.map {
                            CloudFront.Distribution.DistributionConfigProperty.OriginProperty(
                                CustomOriginConfig = it.customOriginConfig?.let {
                                    CloudFront.Distribution.DistributionConfigProperty.OriginProperty.CustomOriginProperty(
                                        HTTPPort             = it.httpPort?.toString(),
                                        HTTPSPort            = it.httpsPort?.toString(),
                                        OriginProtocolPolicy = it.originProtocolPolicy,
                                        OriginSSLProtocols   = it.originSslProtocols?.items
                                    )
                                },
                                DomainName          = it.domainName,
                                Id                  = it.id,
                                OriginCustomHeaders = it.customHeaders?.items?.map {
                                    CloudFront.Distribution.DistributionConfigProperty.OriginProperty.OriginCustomHeaderProperty(
                                        HeaderName  = it.headerName,
                                        HeaderValue = it.headerValue
                                    )
                                },
                                OriginPath     = it.originPath,
                                S3OriginConfig = it.s3OriginConfig?.let {
                                    CloudFront.Distribution.DistributionConfigProperty.OriginProperty.S3OriginProperty(
                                        OriginAccessIdentity = it.originAccessIdentity
                                    )
                                }
                            )
                        },
                        PriceClass   = it.priceClass,
                        Restrictions = it.restrictions?.let {
                            CloudFront.Distribution.DistributionConfigProperty.RestrictionProperty(
                                GeoRestriction = it.geoRestriction.let {
                                    CloudFront.Distribution.DistributionConfigProperty.RestrictionProperty.GeoRestrictionProperty(
                                        Locations       = it.items,
                                        RestrictionType = it.restrictionType
                                    )
                                }
                            )
                        },
                        ViewerCertificate = it.viewerCertificate?.let {
                            CloudFront.Distribution.DistributionConfigProperty.ViewerCertificateProperty(
                                AcmCertificateArn            = it.acmCertificateArn,
                                CloudFrontDefaultCertificate = it.cloudFrontDefaultCertificate?.toString(),
                                IamCertificateId             = it.iamCertificateId,
                                MinimumProtocolVersion       = it.minimumProtocolVersion,
                                SslSupportMethod             = it.sslSupportMethod
                            )
                        },
                        WebACLId = it.webACLId
                    )
                }
            )
        }

}

