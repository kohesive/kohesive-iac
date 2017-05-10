package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.kinesisfirehose.model.CloudWatchLoggingOptions
import com.amazonaws.services.kinesisfirehose.model.CreateDeliveryStreamRequest
import com.amazonaws.services.kinesisfirehose.model.ElasticsearchBufferingHints
import com.amazonaws.services.kinesisfirehose.model.S3DestinationConfiguration
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.KinesisFirehose

class KinesisFirehoseDeliveryStreamResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDeliveryStreamRequest> {

    override val requestClazz = CreateDeliveryStreamRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDeliveryStreamRequest).let {
            KinesisFirehose.DeliveryStream(
                DeliveryStreamName                    = request.deliveryStreamName,
                ElasticsearchDestinationConfiguration = request.elasticsearchDestinationConfiguration?.let {
                    KinesisFirehose.DeliveryStream.ElasticsearchDestinationConfigurationProperty(
                        BufferingHints           = it.bufferingHints.toCF(),
                        RoleARN                  = it.roleARN,
                        IndexName                = it.indexName,
                        CloudWatchLoggingOptions = it.cloudWatchLoggingOptions?.toCF(),
                        DomainARN                = it.domainARN,
                        IndexRotationPeriod      = it.indexRotationPeriod,
                        RetryOptions             = it.retryOptions?.let {
                            KinesisFirehose.DeliveryStream.ElasticsearchDestinationConfigurationProperty.RetryOptionProperty(
                                DurationInSeconds = it.durationInSeconds.toString()
                            )
                        },
                        S3BackupMode    = it.s3BackupMode,
                        S3Configuration = it.s3Configuration?.toCF(),
                        TypeName        = it.typeName
                    )
                },
                RedshiftDestinationConfiguration = request.redshiftDestinationConfiguration?.let {
                    KinesisFirehose.DeliveryStream.RedshiftDestinationConfigurationProperty(
                        CloudWatchLoggingOptions = it.cloudWatchLoggingOptions?.toCF(),
                        RoleARN                  = it.roleARN,
                        S3Configuration          = it.s3Configuration.toCF(),
                        Password                 = it.password,
                        ClusterJDBCURL           = it.clusterJDBCURL,
                        CopyCommand              = it.copyCommand.let {
                            KinesisFirehose.DeliveryStream.RedshiftDestinationConfigurationProperty.CopyCommandProperty(
                                CopyOptions      = it.copyOptions,
                                DataTableColumns = it.dataTableColumns,
                                DataTableName    = it.dataTableName
                            )
                        },
                        Username = it.username
                    )
                },
                S3DestinationConfiguration = request.s3DestinationConfiguration?.toCF()
            )
        }

    fun ElasticsearchBufferingHints.toCF() = KinesisFirehose.DeliveryStream.ElasticsearchDestinationConfigurationProperty.BufferingHintProperty(
        IntervalInSeconds = intervalInSeconds.toString(),
        SizeInMBs         = sizeInMBs.toString()
    )

    fun CloudWatchLoggingOptions.toCF() = KinesisFirehose.DeliveryStream.ElasticsearchDestinationConfigurationProperty.CloudWatchLoggingOptionProperty(
        Enabled       = enabled?.toString(),
        LogGroupName  = logGroupName,
        LogStreamName = logStreamName
    )

    fun S3DestinationConfiguration.toCF() = KinesisFirehose.DeliveryStream.S3DestinationConfigurationProperty(
        BucketARN                = bucketARN,
        CloudWatchLoggingOptions = cloudWatchLoggingOptions?.toCF(),
        RoleARN                  = roleARN,
        BufferingHints           = bufferingHints.let {
            KinesisFirehose.DeliveryStream.S3DestinationConfigurationProperty.BufferingHintProperty(
                IntervalInSeconds = it.intervalInSeconds.toString(),
                SizeInMBs         = it.sizeInMBs.toString()
            )
        },
        Prefix                  = prefix,
        CompressionFormat       = compressionFormat,
        EncryptionConfiguration = encryptionConfiguration?.let {
            KinesisFirehose.DeliveryStream.S3DestinationConfigurationProperty.EncryptionConfigurationProperty(
                KMSEncryptionConfig = it.kmsEncryptionConfig?.let {
                    KinesisFirehose.DeliveryStream.S3DestinationConfigurationProperty.EncryptionConfigurationProperty.KMSEncryptionConfigProperty(
                        AWSKMSKeyARN = it.awskmsKeyARN
                    )
                },
                NoEncryptionConfig = it.noEncryptionConfig
            )
        }
    )

}

