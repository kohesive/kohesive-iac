package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object CloudFront {

    @CloudFormationType("AWS::CloudFront::Distribution")
    data class Distribution(
        val DistributionConfig: Distribution.DistributionConfigProperty
    ) : ResourceProperties {

        data class DistributionConfigProperty(
            val Aliases: List<String>? = null,
            val CacheBehaviors: List<CloudFront.Distribution.DistributionConfigProperty.CacheBehaviorProperty>? = null,
            val Comment: String? = null,
            val CustomErrorResponses: List<CloudFront.Distribution.DistributionConfigProperty.CustomErrorResponseProperty>? = null,
            val DefaultCacheBehavior: Distribution.DistributionConfigProperty.DefaultCacheBehaviorProperty,
            val DefaultRootObject: String? = null,
            val Enabled: String,
            val HttpVersion: String? = null,
            val Logging: Distribution.DistributionConfigProperty.LoggingProperty? = null,
            val Origins: List<CloudFront.Distribution.DistributionConfigProperty.OriginProperty>,
            val PriceClass: String? = null,
            val Restrictions: Distribution.DistributionConfigProperty.RestrictionProperty? = null,
            val ViewerCertificate: Distribution.DistributionConfigProperty.ViewerCertificateProperty? = null,
            val WebACLId: String? = null
        ) {

            data class CacheBehaviorProperty(
                val AllowedMethods: List<String>? = null,
                val CachedMethods: List<String>? = null,
                val Compress: String? = null,
                val DefaultTTL: String? = null,
                val ForwardedValues: Distribution.DistributionConfigProperty.DefaultCacheBehaviorProperty.ForwardedValueProperty,
                val MaxTTL: String? = null,
                val MinTTL: String? = null,
                val PathPattern: String,
                val SmoothStreaming: String? = null,
                val TargetOriginId: String,
                val TrustedSigners: List<String>? = null,
                val ViewerProtocolPolicy: String
            ) 


            data class CustomErrorResponseProperty(
                val ErrorCachingMinTTL: String? = null,
                val ErrorCode: String,
                val ResponseCode: String? = null,
                val ResponsePagePath: String? = null
            ) 


            data class DefaultCacheBehaviorProperty(
                val AllowedMethods: List<String>? = null,
                val CachedMethods: List<String>? = null,
                val Compress: String? = null,
                val DefaultTTL: String? = null,
                val ForwardedValues: Distribution.DistributionConfigProperty.DefaultCacheBehaviorProperty.ForwardedValueProperty,
                val MaxTTL: String? = null,
                val MinTTL: String? = null,
                val SmoothStreaming: String? = null,
                val TargetOriginId: String,
                val TrustedSigners: List<String>? = null,
                val ViewerProtocolPolicy: String
            ) {

                data class ForwardedValueProperty(
                    val Cookies: Distribution.DistributionConfigProperty.DefaultCacheBehaviorProperty.ForwardedValueProperty.CookieProperty? = null,
                    val Headers: List<String>? = null,
                    val QueryString: String,
                    val QueryStringCacheKeys: List<String>? = null
                ) {

                    data class CookieProperty(
                        val Forward: String,
                        val WhitelistedNames: List<String>? = null
                    ) 

                }

            }


            data class LoggingProperty(
                val Bucket: String,
                val IncludeCookies: String? = null,
                val Prefix: String? = null
            ) 


            data class OriginProperty(
                val CustomOriginConfig: Distribution.DistributionConfigProperty.OriginProperty.CustomOriginProperty? = null,
                val DomainName: String,
                val Id: String,
                val OriginCustomHeaders: List<CloudFront.Distribution.DistributionConfigProperty.OriginProperty.OriginCustomHeaderProperty>? = null,
                val OriginPath: String? = null,
                val S3OriginConfig: Distribution.DistributionConfigProperty.OriginProperty.S3OriginProperty? = null
            ) {

                data class CustomOriginProperty(
                    val HTTPPort: String? = null,
                    val HTTPSPort: String? = null,
                    val OriginProtocolPolicy: String,
                    val OriginSSLProtocols: List<String>? = null
                ) 


                data class OriginCustomHeaderProperty(
                    val HeaderName: String,
                    val HeaderValue: String
                ) 


                data class S3OriginProperty(
                    val OriginAccessIdentity: String? = null
                ) 

            }


            data class RestrictionProperty(
                val GeoRestriction: Distribution.DistributionConfigProperty.RestrictionProperty.GeoRestrictionProperty
            ) {

                data class GeoRestrictionProperty(
                    val Locations: List<String>? = null,
                    val RestrictionType: String
                ) 

            }


            data class ViewerCertificateProperty(
                val AcmCertificateArn: String? = null,
                val CloudFrontDefaultCertificate: String? = null,
                val IamCertificateId: String? = null,
                val MinimumProtocolVersion: String? = null,
                val SslSupportMethod: String? = null
            ) 

        }

    }


}