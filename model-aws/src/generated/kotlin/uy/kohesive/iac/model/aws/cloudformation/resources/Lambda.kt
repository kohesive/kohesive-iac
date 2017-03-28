package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object Lambda {

    @CloudFormationType("AWS::Lambda::EventSourceMapping")
    data class EventSourceMapping(
        val BatchSize: String? = null,
        val Enabled: String? = null,
        val EventSourceArn: String,
        val FunctionName: String,
        val StartingPosition: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::Lambda::Alias")
    data class Alias(
        val Description: String? = null,
        val FunctionName: String,
        val FunctionVersion: String,
        val Name: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::Lambda::Function")
    data class Function(
        val Code: Function.CodeProperty,
        val Description: String? = null,
        val Environment: Function.EnvironmentProperty? = null,
        val FunctionName: String? = null,
        val Handler: String,
        val KmsKeyArn: String? = null,
        val MemorySize: String? = null,
        val Role: String,
        val Runtime: String,
        val Timeout: String? = null,
        val VpcConfig: Function.VPCConfigProperty? = null
    ) : ResourceProperties {

        data class CodeProperty(
            val S3Bucket: String? = null,
            val S3Key: String? = null,
            val S3ObjectVersion: String? = null,
            val ZipFile: String? = null
        ) 


        data class EnvironmentProperty(
            val Variables: Map<String, String>? = null
        ) 


        data class VPCConfigProperty(
            val SecurityGroupIds: List<String>,
            val SubnetIds: List<String>
        ) 

    }

    @CloudFormationType("AWS::Lambda::Permission")
    data class Permission(
        val Action: String,
        val FunctionName: String,
        val Principal: String,
        val SourceAccount: String? = null,
        val SourceArn: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::Lambda::Version")
    data class Version(
        val CodeSha256: String? = null,
        val Description: String? = null,
        val FunctionName: String
    ) : ResourceProperties 


}