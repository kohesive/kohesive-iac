package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.s3.model.*
import com.amazonaws.util.DateUtils
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.S3

class S3BucketResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateBucketRequest> {

    override val requestClazz = CreateBucketRequest::class

    fun Filter.toCF() = S3.Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty.FilterProperty(
        S3Key = s3KeyFilter.let {
            S3.Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty.FilterProperty.S3KeyProperty(
                Rules = it.filterRules.map {
                    S3.Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty.FilterProperty.S3KeyProperty.RuleProperty(
                        Name  = it.name,
                        Value = it.value
                    )
                }
            )
        }
    )

    fun BucketLifecycleConfiguration.NoncurrentVersionTransition.toCF() = S3.Bucket.LifecycleConfigurationProperty.RuleProperty.NoncurrentVersionTransitionProperty(
        StorageClass     = storageClassAsString,
        TransitionInDays = days.toString()
    )

    fun BucketLifecycleConfiguration.Transition.toCF() = S3.Bucket.LifecycleConfigurationProperty.RuleProperty.TransitionProperty(
        StorageClass     = storageClassAsString,
        TransitionInDays = days.toString(),
        TransitionDate   = date?.let { DateUtils.formatISO8601Date(it) }
    )

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateBucketRequest).let {
            S3.Bucket(
                BucketName        = request.bucketName,
                AccessControl     = request.cannedAcl?.name,
                CorsConfiguration = relatedObjects.filterIsInstance<SetBucketCrossOriginConfigurationRequest>().lastOrNull()?.crossOriginConfiguration?.let {
                    S3.Bucket.CorsConfigurationProperty(
                        CorsRules = it.rules.map {
                           S3.Bucket.CorsConfigurationProperty.CrossoriginAccessRuleProperty(
                               Id             = it.id,
                               AllowedMethods = it.allowedMethods.map { it.name },
                               AllowedHeaders = it.allowedHeaders,
                               AllowedOrigins = it.allowedOrigins,
                               ExposedHeaders = it.exposedHeaders,
                               MaxAge         = it.maxAgeSeconds.toString()
                           )
                        }
                    )
                },
                LifecycleConfiguration = relatedObjects.filterIsInstance<SetBucketLifecycleConfigurationRequest>().lastOrNull()?.let {
                    S3.Bucket.LifecycleConfigurationProperty(
                        Rules = it.lifecycleConfiguration.rules.map {
                            S3.Bucket.LifecycleConfigurationProperty.RuleProperty(
                                ExpirationDate                    = it.expirationDate?.let { DateUtils.formatISO8601Date(it) },
                                Id                                = it.id,
                                Prefix                            = it.prefix,
                                Status                            = it.status,
                                ExpirationInDays                  = it.expirationInDays.toString(),
                                NoncurrentVersionExpirationInDays = it.noncurrentVersionExpirationInDays.toString(),
                                NoncurrentVersionTransitions      = it.noncurrentVersionTransitions?.map { it.toCF() },
                                NoncurrentVersionTransition       = it.noncurrentVersionTransition?.toCF(),
                                Transition                        = it.transition.toCF(),
                                Transitions                       = it.transitions?.map { it.toCF() }
                            )
                        }
                    )
                },
                LoggingConfiguration = relatedObjects.filterIsInstance<SetBucketLoggingConfigurationRequest>().lastOrNull()?.let {
                    S3.Bucket.LoggingConfigurationProperty(
                        DestinationBucketName = it.loggingConfiguration.destinationBucketName,
                        LogFilePrefix         = it.loggingConfiguration.logFilePrefix
                    )
                },
                NotificationConfiguration = relatedObjects.filterIsInstance<SetBucketNotificationConfigurationRequest>().lastOrNull()?.let {
                    val notificationConfigurations = it.notificationConfiguration.configurations.values
                    S3.Bucket.NotificationConfigurationProperty(
                        LambdaConfigurations = notificationConfigurations.filterIsInstance<LambdaConfiguration>().flatMap { lambdaConfig ->
                            lambdaConfig.events.map { event ->
                                S3.Bucket.NotificationConfigurationProperty.LambdaConfigurationProperty(
                                    Event    = event,
                                    Filter   = lambdaConfig.filter?.toCF(),
                                    Function = lambdaConfig.functionARN
                                )
                            }
                        },
                        QueueConfigurations = notificationConfigurations.filterIsInstance<QueueConfiguration>().flatMap { queueConfig ->
                            queueConfig.events.map { event ->
                                S3.Bucket.NotificationConfigurationProperty.QueueConfigurationProperty(
                                    Event  = event,
                                    Filter = queueConfig.filter?.toCF(),
                                    Queue  = queueConfig.queueARN
                                )
                            }
                        },
                        TopicConfigurations = notificationConfigurations.filterIsInstance<TopicConfiguration>().flatMap { topicConfig ->
                            topicConfig.events.map { event ->
                                S3.Bucket.NotificationConfigurationProperty.TopicConfigurationProperty(
                                    Event  = event,
                                    Filter = topicConfig.filter?.toCF(),
                                    Topic  = topicConfig.topicARN
                                )
                            }
                        }
                    )
                },
                ReplicationConfiguration = relatedObjects.filterIsInstance<SetBucketReplicationConfigurationRequest>().lastOrNull()?.let {
                    S3.Bucket.ReplicationConfigurationProperty(
                        Role  = it.replicationConfiguration.roleARN,
                        Rules = it.replicationConfiguration.rules.map {
                            S3.Bucket.ReplicationConfigurationProperty.RuleProperty(
                                Destination = it.value.destinationConfig.let {
                                    S3.Bucket.ReplicationConfigurationProperty.RuleProperty.DestinationProperty(
                                        Bucket       = it.bucketARN,
                                        StorageClass = it.storageClass
                                    )
                                },
                                Status = it.value.status,
                                Prefix = it.value.prefix,
                                Id     = it.key
                            )
                        }
                    )
                },
                VersioningConfiguration = relatedObjects.filterIsInstance<SetBucketVersioningConfigurationRequest>().lastOrNull()?.let {
                    S3.Bucket.VersioningConfigurationProperty(
                        Status = it.versioningConfiguration.status
                    )
                },
                WebsiteConfiguration = relatedObjects.filterIsInstance<SetBucketWebsiteConfigurationRequest>().lastOrNull()?.configuration?.let {
                    S3.Bucket.WebsiteConfigurationProperty(
                        ErrorDocument         = it.errorDocument,
                        IndexDocument         = it.indexDocumentSuffix,
                        RedirectAllRequestsTo = it.redirectAllRequestsTo?.let {
                            S3.Bucket.WebsiteConfigurationProperty.RedirectAllRequestsToProperty(
                                HostName = it.hostName,
                                Protocol = it.getprotocol()
                            )
                        },
                        RoutingRules = it.routingRules?.map {
                            S3.Bucket.WebsiteConfigurationProperty.RoutingRuleProperty(
                                RedirectRule = it.redirect.let {
                                    S3.Bucket.WebsiteConfigurationProperty.RoutingRuleProperty.RedirectRuleProperty(
                                        HostName             = it.hostName,
                                        Protocol             = it.getprotocol(),
                                        HttpRedirectCode     = it.httpRedirectCode,
                                        ReplaceKeyPrefixWith = it.replaceKeyPrefixWith,
                                        ReplaceKeyWith       = it.replaceKeyWith
                                    )
                                },
                                RoutingRuleCondition = it.condition?.let {
                                    S3.Bucket.WebsiteConfigurationProperty.RoutingRuleProperty.RoutingRuleConditionProperty(
                                        HttpErrorCodeReturnedEquals = it.httpErrorCodeReturnedEquals,
                                        KeyPrefixEquals             = it.keyPrefixEquals
                                    )
                                }
                            )
                        }
                    )
                },
                Tags = relatedObjects.filterIsInstance<BucketTaggingConfiguration>().flatMap { it.allTagSets }.flatMap { it.allTags.toList() }.map {
                    CloudFormation.ResourceTag(
                        Key   = it.first,
                        Value = it.second
                    )
                }
            )
        }

}

