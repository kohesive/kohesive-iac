package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.lambda.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.Lambda

class LambdaVersionResourcePropertiesBuilder : ResourcePropertiesBuilder<PublishVersionRequest> {

    override val requestClazz = PublishVersionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PublishVersionRequest).let {
            Lambda.Version(
                CodeSha256   = it.codeSha256,
                FunctionName = it.functionName,
                Description  = it.description
            )
        }

}

class LambdaPermissionResourcePropertiesBuilder : ResourcePropertiesBuilder<AddPermissionRequest> {

    override val requestClazz = AddPermissionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as AddPermissionRequest).let {
            Lambda.Permission(
                Action        = it.action,
                Principal     = it.principal,
                FunctionName  = it.functionName,
                SourceAccount = it.sourceAccount,
                SourceArn     = it.sourceArn
            )
        }
}

class LambdaAliasResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateAliasRequest> {

    override val requestClazz = CreateAliasRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAliasRequest).let {
            Lambda.Alias(
                Description     = request.description,
                FunctionName    = request.functionName,
                FunctionVersion = request.functionVersion,
                Name            = request.name
            )
        }

}

class LambdaEventSourceMappingResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateEventSourceMappingRequest> {

    override val requestClazz = CreateEventSourceMappingRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateEventSourceMappingRequest).let {
            Lambda.EventSourceMapping(
                BatchSize        = request.batchSize?.toString(),
                Enabled          = request.enabled?.toString(),
                EventSourceArn   = request.eventSourceArn,
                FunctionName     = request.functionName,
                StartingPosition = request.startingPosition
            )
        }

}

class LambdaFunctionResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateFunctionRequest> {

    override val requestClazz = CreateFunctionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateFunctionRequest).let {
            Lambda.Function(
                Code = request.code.let {
                    Lambda.Function.CodeProperty(
                        S3Key           = it.s3Key,
                        S3Bucket        = it.s3Bucket,
                        S3ObjectVersion = it.s3ObjectVersion,
                        ZipFile = it.zipFile?.let {
                            String(it.array())
                        }
                    )
                },
                Description = request.description,
                Environment = request.environment?.let {
                    Lambda.Function.EnvironmentProperty(
                        Variables = it.variables
                    )
                },
                FunctionName = request.functionName,
                Handler      = request.handler,
                KmsKeyArn    = request.kmsKeyArn,
                MemorySize   = request.memorySize?.toString(),
                Role         = request.role,
                Runtime      = request.runtime,
                Timeout      = request.timeout?.toString(),
                VpcConfig    = request.vpcConfig?.let {
                    Lambda.Function.VPCConfigProperty(
                        SecurityGroupIds = it.securityGroupIds,
                        SubnetIds        = it.subnetIds
                    )
                }
            )
        }

}

