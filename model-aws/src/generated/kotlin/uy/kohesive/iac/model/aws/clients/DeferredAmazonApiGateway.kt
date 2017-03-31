package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.apigateway.AbstractAmazonApiGateway
import com.amazonaws.services.apigateway.AmazonApiGateway
import com.amazonaws.services.apigateway.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonApiGateway(val context: IacContext) : AbstractAmazonApiGateway(), AmazonApiGateway {

    override fun createApiKey(request: CreateApiKeyRequest): CreateApiKeyResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateApiKeyRequest, CreateApiKeyResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateApiKeyRequest::getValue to CreateApiKeyResult::getValue,
                    CreateApiKeyRequest::getName to CreateApiKeyResult::getName,
                    CreateApiKeyRequest::getCustomerId to CreateApiKeyResult::getCustomerId,
                    CreateApiKeyRequest::getDescription to CreateApiKeyResult::getDescription,
                    CreateApiKeyRequest::getEnabled to CreateApiKeyResult::getEnabled
                )
            )
        }
    }

    override fun createAuthorizer(request: CreateAuthorizerRequest): CreateAuthorizerResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateAuthorizerRequest, CreateAuthorizerResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateAuthorizerRequest::getName to CreateAuthorizerResult::getName,
                    CreateAuthorizerRequest::getType to CreateAuthorizerResult::getType,
                    CreateAuthorizerRequest::getProviderARNs to CreateAuthorizerResult::getProviderARNs,
                    CreateAuthorizerRequest::getAuthType to CreateAuthorizerResult::getAuthType,
                    CreateAuthorizerRequest::getAuthorizerUri to CreateAuthorizerResult::getAuthorizerUri,
                    CreateAuthorizerRequest::getAuthorizerCredentials to CreateAuthorizerResult::getAuthorizerCredentials,
                    CreateAuthorizerRequest::getIdentitySource to CreateAuthorizerResult::getIdentitySource,
                    CreateAuthorizerRequest::getIdentityValidationExpression to CreateAuthorizerResult::getIdentityValidationExpression,
                    CreateAuthorizerRequest::getAuthorizerResultTtlInSeconds to CreateAuthorizerResult::getAuthorizerResultTtlInSeconds
                )
            )
        }
    }

    override fun createBasePathMapping(request: CreateBasePathMappingRequest): CreateBasePathMappingResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateBasePathMappingRequest, CreateBasePathMappingResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateBasePathMappingRequest::getBasePath to CreateBasePathMappingResult::getBasePath,
                    CreateBasePathMappingRequest::getRestApiId to CreateBasePathMappingResult::getRestApiId,
                    CreateBasePathMappingRequest::getStage to CreateBasePathMappingResult::getStage
                )
            )
        }
    }

    override fun createDeployment(request: CreateDeploymentRequest): CreateDeploymentResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDeploymentRequest, CreateDeploymentResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDeploymentRequest::getDescription to CreateDeploymentResult::getDescription
                )
            )
        }
    }

    override fun createDocumentationPart(request: CreateDocumentationPartRequest): CreateDocumentationPartResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDocumentationPartRequest, CreateDocumentationPartResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDocumentationPartRequest::getLocation to CreateDocumentationPartResult::getLocation,
                    CreateDocumentationPartRequest::getProperties to CreateDocumentationPartResult::getProperties
                )
            )
        }
    }

    override fun createDocumentationVersion(request: CreateDocumentationVersionRequest): CreateDocumentationVersionResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDocumentationVersionRequest, CreateDocumentationVersionResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDocumentationVersionRequest::getDescription to CreateDocumentationVersionResult::getDescription
                )
            )
        }
    }

    override fun createDomainName(request: CreateDomainNameRequest): CreateDomainNameResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDomainNameRequest, CreateDomainNameResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDomainNameRequest::getDomainName to CreateDomainNameResult::getDomainName,
                    CreateDomainNameRequest::getCertificateName to CreateDomainNameResult::getCertificateName,
                    CreateDomainNameRequest::getCertificateArn to CreateDomainNameResult::getCertificateArn
                )
            )
        }
    }

    override fun createModel(request: CreateModelRequest): CreateModelResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateModelRequest, CreateModelResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateModelRequest::getName to CreateModelResult::getName,
                    CreateModelRequest::getDescription to CreateModelResult::getDescription,
                    CreateModelRequest::getSchema to CreateModelResult::getSchema,
                    CreateModelRequest::getContentType to CreateModelResult::getContentType
                )
            )
        }
    }

    override fun createResource(request: CreateResourceRequest): CreateResourceResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateResourceRequest, CreateResourceResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateResourceRequest::getParentId to CreateResourceResult::getParentId,
                    CreateResourceRequest::getPathPart to CreateResourceResult::getPathPart
                )
            )
        }
    }

    override fun createRestApi(request: CreateRestApiRequest): CreateRestApiResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateRestApiRequest, CreateRestApiResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateRestApiRequest::getName to CreateRestApiResult::getName,
                    CreateRestApiRequest::getDescription to CreateRestApiResult::getDescription,
                    CreateRestApiRequest::getVersion to CreateRestApiResult::getVersion,
                    CreateRestApiRequest::getBinaryMediaTypes to CreateRestApiResult::getBinaryMediaTypes
                )
            )
        }
    }

    override fun createStage(request: CreateStageRequest): CreateStageResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateStageRequest, CreateStageResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateStageRequest::getDeploymentId to CreateStageResult::getDeploymentId,
                    CreateStageRequest::getStageName to CreateStageResult::getStageName,
                    CreateStageRequest::getDescription to CreateStageResult::getDescription,
                    CreateStageRequest::getCacheClusterEnabled to CreateStageResult::getCacheClusterEnabled,
                    CreateStageRequest::getCacheClusterSize to CreateStageResult::getCacheClusterSize,
                    CreateStageRequest::getVariables to CreateStageResult::getVariables,
                    CreateStageRequest::getDocumentationVersion to CreateStageResult::getDocumentationVersion
                )
            )
        }
    }

    override fun createUsagePlan(request: CreateUsagePlanRequest): CreateUsagePlanResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateUsagePlanRequest, CreateUsagePlanResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateUsagePlanRequest::getName to CreateUsagePlanResult::getName,
                    CreateUsagePlanRequest::getDescription to CreateUsagePlanResult::getDescription,
                    CreateUsagePlanRequest::getApiStages to CreateUsagePlanResult::getApiStages,
                    CreateUsagePlanRequest::getThrottle to CreateUsagePlanResult::getThrottle,
                    CreateUsagePlanRequest::getQuota to CreateUsagePlanResult::getQuota
                )
            )
        }
    }

    override fun createUsagePlanKey(request: CreateUsagePlanKeyRequest): CreateUsagePlanKeyResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateUsagePlanKeyRequest, CreateUsagePlanKeyResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonApiGateway(context: IacContext) : BaseDeferredAmazonApiGateway(context)
