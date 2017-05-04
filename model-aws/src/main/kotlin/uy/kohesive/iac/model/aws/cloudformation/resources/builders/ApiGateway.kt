package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.apigateway.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.ApiGateway

// TODO: missing resources - 'AWS::ApiGateway::Account'

class ApiGatewayClientCertificatePropertiesBuilder : ResourcePropertiesBuilder<GenerateClientCertificateRequest> {

    override val requestClazz = GenerateClientCertificateRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as GenerateClientCertificateRequest).let {
            ApiGateway.ClientCertificate(
                Description = it.description
            )
        }
}

class ApiGatewayApiKeyResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateApiKeyRequest> {

    override val requestClazz = CreateApiKeyRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateApiKeyRequest).let {
            ApiGateway.ApiKey(
                Description = request.description,
                Enabled     = request.enabled?.toString(),
                Name        = request.name,
                StageKeys   = request.stageKeys?.map {
                    ApiGateway.ApiKey.StageKeyProperty(
                        RestApiId = it.restApiId,
                        StageName = it.stageName
                    )
                }
            )
        }

}

class ApiGatewayAuthorizerResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateAuthorizerRequest> {

    override val requestClazz = CreateAuthorizerRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAuthorizerRequest).let {
            ApiGateway.Authorizer(
                AuthorizerCredentials        = request.authorizerCredentials,
                AuthorizerResultTtlInSeconds = request.authorizerResultTtlInSeconds?.toString(),
                AuthorizerUri                = request.authorizerUri,
                IdentitySource               = request.identitySource,
                IdentityValidationExpression = request.identityValidationExpression,
                Name                         = request.name,
                ProviderARNs                 = request.providerARNs,
                RestApiId                    = request.restApiId,
                Type                         = request.type
            )
        }

}

class ApiGatewayBasePathMappingResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateBasePathMappingRequest> {

    override val requestClazz = CreateBasePathMappingRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateBasePathMappingRequest).let {
            ApiGateway.BasePathMapping(
                BasePath   = request.basePath,
                DomainName = request.domainName,
                RestApiId  = request.restApiId,
                Stage      = request.stage
            )
        }

}

class ApiGatewayDeploymentResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDeploymentRequest> {

    override val requestClazz = CreateDeploymentRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDeploymentRequest).let {
            ApiGateway.Deployment(
                Description      = request.description,
                RestApiId        = request.restApiId,
                // TODO: figure this out
                // StageDescription = request.stageDescription,
                StageName        = request.stageName
            )
        }

}

class ApiGatewayMethodResourcePropertiesBuilder : ResourcePropertiesBuilder<PutMethodRequest> {

    override val requestClazz = PutMethodRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutMethodRequest).let {
            ApiGateway.Method(
                ApiKeyRequired    = request.apiKeyRequired?.toString(),
                AuthorizationType = request.authorizationType,
                AuthorizerId      = request.authorizerId,
                HttpMethod        = request.httpMethod,
                RequestModels     = request.requestModels,
                RequestParameters = request.requestParameters.mapValues { it.value.toString() },
                ResourceId        = request.resourceId,
                RestApiId         = request.restApiId,
                Integration       = relatedObjects.filterIsInstance<PutIntegrationRequest>().firstOrNull()?.let { integrationRequest ->
                    ApiGateway.Method.IntegrationProperty(
                        CacheKeyParameters    = integrationRequest.cacheKeyParameters,
                        Type                  = integrationRequest.type,
                        CacheNamespace        = integrationRequest.cacheNamespace,
                        Credentials           = integrationRequest.credentials,
                        IntegrationHttpMethod = integrationRequest.integrationHttpMethod,
                        PassthroughBehavior   = integrationRequest.passthroughBehavior,
                        RequestParameters     = integrationRequest.requestParameters,
                        RequestTemplates      = integrationRequest.requestTemplates,
                        Uri                   = integrationRequest.uri,
                        IntegrationResponses  = relatedObjects.filterIsInstance<PutIntegrationResponseRequest>().map { integrationResponseRequest ->
                            ApiGateway.Method.IntegrationProperty.IntegrationResponseProperty(
                                ResponseParameters = integrationResponseRequest.responseParameters,
                                ResponseTemplates  = integrationResponseRequest.responseTemplates,
                                SelectionPattern   = integrationResponseRequest.selectionPattern,
                                StatusCode         = integrationResponseRequest.statusCode
                            )
                        }
                    )
                },
                MethodResponses = relatedObjects.filterIsInstance<PutMethodResponseRequest>().map { responseRequest ->
                    ApiGateway.Method.MethodResponseProperty(
                        ResponseModels     = responseRequest.responseModels,
                        ResponseParameters = responseRequest.responseParameters?.mapValues { it.value.toString() },
                        StatusCode         = responseRequest.statusCode
                    )
                }
            )
        }

}

class ApiGatewayModelResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateModelRequest> {

    override val requestClazz = CreateModelRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateModelRequest).let {
            ApiGateway.Model(
                ContentType = request.contentType,
                Description = request.description,
                Name        = request.name,
                RestApiId   = request.restApiId,
                Schema      = request.schema
            )
        }

}

class ApiGatewayResourceResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateResourceRequest> {

    override val requestClazz = CreateResourceRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateResourceRequest).let {
            ApiGateway.Resource(
                ParentId  = request.parentId,
                PathPart  = request.pathPart,
                RestApiId = request.restApiId
            )
        }

}

class ApiGatewayRestApiResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateRestApiRequest> {

    override val requestClazz = CreateRestApiRequest::class

    // TODO: missing properties - 'Body', 'BodyS3Location', 'FailOnWarnings', 'Parameters'
    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateRestApiRequest).let {
            ApiGateway.RestApi(
                CloneFrom   = request.cloneFrom,
                Description = request.description,
                Name        = request.name
            )
        }

}

class ApiGatewayStageResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateStageRequest> {

    override val requestClazz = CreateStageRequest::class

    // TODO: missing property - 'ClientCertificateId' (https://github.com/aws/aws-sdk-java/issues/1146)
    // TODO: missing property - 'MethodSettings' (???)
    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateStageRequest).let {
            ApiGateway.Stage(
                CacheClusterEnabled = request.cacheClusterEnabled?.toString(),
                CacheClusterSize    = request.cacheClusterSize,
                DeploymentId        = request.deploymentId,
                Description         = request.description,
                RestApiId           = request.restApiId,
                StageName           = request.stageName,
                Variables           = request.variables
            )
        }

}

class ApiGatewayUsagePlanResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateUsagePlanRequest> {

    override val requestClazz = CreateUsagePlanRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateUsagePlanRequest).let {
            ApiGateway.UsagePlan(
                UsagePlanName = it.name,
                ApiStages     = request.apiStages?.map {
                    ApiGateway.UsagePlan.ApiStageProperty(
                        ApiId = it.apiId,
                        Stage = it.stage
                    )
                },
                Description = request.description,
                Quota       = request.quota?.let {
                    ApiGateway.UsagePlan.QuotaSettingProperty(
                        Limit  = it.limit?.toString(),
                        Offset = it.offset?.toString(),
                        Period = it.period
                    )
                },
                Throttle    = request.throttle?.let {
                    ApiGateway.UsagePlan.ThrottleSettingProperty(
                        BurstLimit = it.burstLimit?.toString(),
                        RateLimit  = it.rateLimit?.toString()
                    )
                }
            )
        }

}

