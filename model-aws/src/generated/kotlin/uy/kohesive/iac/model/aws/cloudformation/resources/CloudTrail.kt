package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object CloudTrail {

    @CloudFormationType("AWS::CloudTrail::Trail")
    data class Trail(
        val CloudWatchLogsLogGroupArn: String? = null,
        val CloudWatchLogsRoleArn: String? = null,
        val EnableLogFileValidation: String? = null,
        val IncludeGlobalServiceEvents: String? = null,
        val IsLogging: String,
        val IsMultiRegionTrail: String? = null,
        val KMSKeyId: String? = null,
        val S3BucketName: String,
        val S3KeyPrefix: String? = null,
        val SnsTopicName: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 


}