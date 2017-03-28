package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object S3 {

    @CloudFormationType("AWS::S3::Bucket")
    data class Bucket(
        val AccessControl: String? = null,
        val BucketName: String? = null,
        val CorsConfiguration: Bucket.CorsConfigurationProperty? = null,
        val LifecycleConfiguration: Bucket.LifecycleConfigurationProperty? = null,
        val LoggingConfiguration: Bucket.LoggingConfigurationProperty? = null,
        val NotificationConfiguration: Bucket.NotificationConfigurationProperty? = null,
        val ReplicationConfiguration: Bucket.ReplicationConfigurationProperty? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val VersioningConfiguration: Bucket.VersioningConfigurationProperty? = null,
        val WebsiteConfiguration: Bucket.WebsiteConfigurationProperty? = null
    ) : ResourceProperties {

        data class CorsConfigurationProperty(
            val CorsRules: List<S3.Bucket.CorsConfigurationProperty.CrossoriginAccessRuleProperty>
        ) {

            data class CrossoriginAccessRuleProperty(
                val AllowedHeaders: List<String>? = null,
                val AllowedMethods: List<String>,
                val AllowedOrigins: List<String>,
                val ExposedHeaders: List<String>? = null,
                val Id: String? = null,
                val MaxAge: String? = null
            ) 

        }


        data class LifecycleConfigurationProperty(
            val Rules: List<S3.Bucket.LifecycleConfigurationProperty.RuleProperty>
        ) {

            data class RuleProperty(
                val ExpirationDate: String? = null,
                val ExpirationInDays: String? = null,
                val Id: String? = null,
                val NoncurrentVersionExpirationInDays: String? = null,
                val NoncurrentVersionTransition: Bucket.LifecycleConfigurationProperty.RuleProperty.NoncurrentVersionTransitionProperty? = null,
                val NoncurrentVersionTransitions: List<S3.Bucket.LifecycleConfigurationProperty.RuleProperty.NoncurrentVersionTransitionProperty>? = null,
                val Prefix: String? = null,
                val Status: String,
                val Transition: Bucket.LifecycleConfigurationProperty.RuleProperty.TransitionProperty? = null,
                val Transitions: List<S3.Bucket.LifecycleConfigurationProperty.RuleProperty.TransitionProperty>? = null
            ) {

                data class NoncurrentVersionTransitionProperty(
                    val StorageClass: String,
                    val TransitionInDays: String
                ) 


                data class TransitionProperty(
                    val StorageClass: String,
                    val TransitionDate: String? = null,
                    val TransitionInDays: String? = null
                ) 

            }

        }


        data class LoggingConfigurationProperty(
            val DestinationBucketName: String? = null,
            val LogFilePrefix: String? = null
        ) 


        data class NotificationConfigurationProperty(
            val LambdaConfigurations: List<S3.Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty>? = null,
            val QueueConfigurations: List<S3.Bucket.NotificationConfigurationProperty.QueueConfigurationProperty>? = null,
            val TopicConfigurations: List<S3.Bucket.NotificationConfigurationProperty.TopicConfigurationProperty>? = null
        ) {

            data class LambdaConfigurationProperty(
                val Event: String,
                val Filter: Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty.FilterProperty? = null,
                val Function: String
            ) {

                data class FilterProperty(
                    val S3Key: Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty.FilterProperty.S3KeyProperty
                ) {

                    data class S3KeyProperty(
                        val Rules: List<S3.Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty.FilterProperty.S3KeyProperty.RuleProperty>
                    ) {

                        data class RuleProperty(
                            val Name: String,
                            val Value: String
                        ) 

                    }

                }

            }


            data class QueueConfigurationProperty(
                val Event: String,
                val Filter: Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty.FilterProperty? = null,
                val Queue: String
            ) 


            data class TopicConfigurationProperty(
                val Event: String,
                val Filter: Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty.FilterProperty? = null,
                val Topic: String
            ) 

        }


        data class ReplicationConfigurationProperty(
            val Role: String,
            val Rules: List<S3.Bucket.ReplicationConfigurationProperty.RuleProperty>
        ) {

            data class RuleProperty(
                val Destination: Bucket.ReplicationConfigurationProperty.RuleProperty.DestinationProperty,
                val Id: String? = null,
                val Prefix: String,
                val Status: String
            ) {

                data class DestinationProperty(
                    val Bucket: String,
                    val StorageClass: String? = null
                ) 

            }

        }


        data class VersioningConfigurationProperty(
            val Status: String
        ) 


        data class WebsiteConfigurationProperty(
            val ErrorDocument: String? = null,
            val IndexDocument: String,
            val RedirectAllRequestsTo: Bucket.WebsiteConfigurationProperty.RedirectAllRequestsToProperty? = null,
            val RoutingRules: List<S3.Bucket.WebsiteConfigurationProperty.RoutingRuleProperty>? = null
        ) {

            data class RedirectAllRequestsToProperty(
                val HostName: String,
                val Protocol: String? = null
            ) 


            data class RoutingRuleProperty(
                val RedirectRule: Bucket.WebsiteConfigurationProperty.RoutingRuleProperty.RedirectRuleProperty,
                val RoutingRuleCondition: Bucket.WebsiteConfigurationProperty.RoutingRuleProperty.RoutingRuleConditionProperty? = null
            ) {

                data class RedirectRuleProperty(
                    val HostName: String? = null,
                    val HttpRedirectCode: String? = null,
                    val Protocol: String? = null,
                    val ReplaceKeyPrefixWith: String? = null,
                    val ReplaceKeyWith: String? = null
                ) 


                data class RoutingRuleConditionProperty(
                    val HttpErrorCodeReturnedEquals: String? = null,
                    val KeyPrefixEquals: String? = null
                ) 

            }

        }

    }

    @CloudFormationType("AWS::S3::BucketPolicy")
    data class BucketPolicy(
        val Bucket: String,
        val PolicyDocument: Any
    ) : ResourceProperties 


}