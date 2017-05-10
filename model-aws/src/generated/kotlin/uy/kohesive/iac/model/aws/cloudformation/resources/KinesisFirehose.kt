package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes
import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties

@CloudFormationTypes
object KinesisFirehose {

    @CloudFormationType("AWS::KinesisFirehose::DeliveryStream")
    data class DeliveryStream(
        val DeliveryStreamName: String? = null,
        val ElasticsearchDestinationConfiguration: DeliveryStream.ElasticsearchDestinationConfigurationProperty? = null,
        val RedshiftDestinationConfiguration: DeliveryStream.RedshiftDestinationConfigurationProperty? = null,
        val S3DestinationConfiguration: DeliveryStream.S3DestinationConfigurationProperty? = null
    ) : ResourceProperties {

        data class ElasticsearchDestinationConfigurationProperty(
            val BufferingHints: DeliveryStream.ElasticsearchDestinationConfigurationProperty.BufferingHintProperty,
            val CloudWatchLoggingOptions: DeliveryStream.ElasticsearchDestinationConfigurationProperty.CloudWatchLoggingOptionProperty? = null,
            val DomainARN: String,
            val IndexName: String,
            val IndexRotationPeriod: String,
            val RetryOptions: RetryOptionProperty? = null,
            val RoleARN: String,
            val S3BackupMode: String,
            val S3Configuration: S3DestinationConfigurationProperty?,
            val TypeName: String
        ) {

            data class BufferingHintProperty(
                val IntervalInSeconds: String,
                val SizeInMBs: String
            ) 


            data class CloudWatchLoggingOptionProperty(
                val Enabled: String? = null,
                val LogGroupName: String? = null,
                val LogStreamName: String? = null
            ) 


            data class RetryOptionProperty(
                val DurationInSeconds: String
            ) 

        }


        data class S3DestinationConfigurationProperty(
            val BucketARN: String,
            val BufferingHints: DeliveryStream.S3DestinationConfigurationProperty.BufferingHintProperty,
            val CloudWatchLoggingOptions: DeliveryStream.ElasticsearchDestinationConfigurationProperty.CloudWatchLoggingOptionProperty? = null,
            val CompressionFormat: String,
            val EncryptionConfiguration: DeliveryStream.S3DestinationConfigurationProperty.EncryptionConfigurationProperty? = null,
            val Prefix: String,
            val RoleARN: String
        ) {

            data class BufferingHintProperty(
                val IntervalInSeconds: String,
                val SizeInMBs: String
            ) 


            data class EncryptionConfigurationProperty(
                val KMSEncryptionConfig: DeliveryStream.S3DestinationConfigurationProperty.EncryptionConfigurationProperty.KMSEncryptionConfigProperty? = null,
                val NoEncryptionConfig: String? = null
            ) {

                data class KMSEncryptionConfigProperty(
                    val AWSKMSKeyARN: String
                ) 

            }

        }


        data class RedshiftDestinationConfigurationProperty(
            val CloudWatchLoggingOptions: DeliveryStream.ElasticsearchDestinationConfigurationProperty.CloudWatchLoggingOptionProperty? = null,
            val ClusterJDBCURL: String,
            val CopyCommand: DeliveryStream.RedshiftDestinationConfigurationProperty.CopyCommandProperty,
            val Password: String,
            val RoleARN: String,
            val S3Configuration: DeliveryStream.S3DestinationConfigurationProperty,
            val Username: String
        ) {

            data class CopyCommandProperty(
                val CopyOptions: String? = null,
                val DataTableColumns: String? = null,
                val DataTableName: String
            ) 

        }

    }


}