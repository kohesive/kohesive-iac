package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object ApiGateway {

    @CloudFormationType("AWS::ApiGateway::Account")
    data class Account(
        val CloudWatchRoleArn: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::ApiGateway::ApiKey")
    data class ApiKey(
        val Description: String? = null,
        val Enabled: String? = null,
        val Name: String? = null,
        val StageKeys: List<ApiGateway.ApiKey.StageKeyProperty>? = null
    ) : ResourceProperties {

        data class StageKeyProperty(
            val RestApiId: String? = null,
            val StageName: String? = null
        ) 

    }

    @CloudFormationType("AWS::ApiGateway::Authorizer")
    data class Authorizer(
        val AuthorizerCredentials: String? = null,
        val AuthorizerResultTtlInSeconds: String? = null,
        val AuthorizerUri: String? = null,
        val IdentitySource: String,
        val IdentityValidationExpression: String? = null,
        val Name: String,
        val ProviderARNs: List<String>? = null,
        val RestApiId: String? = null,
        val Type: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::ApiGateway::BasePathMapping")
    data class BasePathMapping(
        val BasePath: String? = null,
        val DomainName: String,
        val RestApiId: String,
        val Stage: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::ApiGateway::ClientCertificate")
    data class ClientCertificate(
        val Description: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::ApiGateway::Deployment")
    data class Deployment(
        val Description: String? = null,
        val RestApiId: String,
        val StageDescription: Deployment.StageDescriptionProperty? = null,
        val StageName: String? = null
    ) : ResourceProperties {

        data class StageDescriptionProperty(
            val CacheClusterEnabled: String? = null,
            val CacheClusterSize: String? = null,
            val CacheDataEncrypted: String? = null,
            val CacheTtlInSeconds: String? = null,
            val CachingEnabled: String? = null,
            val ClientCertificateId: String? = null,
            val DataTraceEnabled: String? = null,
            val Description: String? = null,
            val LoggingLevel: String? = null,
            val MethodSettings: List<ApiGateway.Deployment.StageDescriptionProperty.MethodSettingProperty>? = null,
            val MetricsEnabled: String? = null,
            val StageName: String? = null,
            val ThrottlingBurstLimit: String? = null,
            val ThrottlingRateLimit: String? = null,
            val Variables: Map<String, String>? = null
        ) {

            data class MethodSettingProperty(
                val CacheDataEncrypted: String? = null,
                val CacheTtlInSeconds: String? = null,
                val CachingEnabled: String? = null,
                val DataTraceEnabled: String? = null,
                val HttpMethod: String? = null,
                val LoggingLevel: String? = null,
                val MetricsEnabled: String? = null,
                val ResourcePath: String? = null,
                val ThrottlingBurstLimit: String? = null,
                val ThrottlingRateLimit: String? = null
            ) 

        }

    }

    @CloudFormationType("AWS::ApiGateway::Method")
    data class Method(
        val ApiKeyRequired: String? = null,
        val AuthorizationType: String? = null,
        val AuthorizerId: String? = null,
        val HttpMethod: String,
        val Integration: Method.IntegrationProperty? = null,
        val MethodResponses: List<ApiGateway.Method.MethodResponseProperty>? = null,
        val RequestModels: Map<String, String>? = null,
        val RequestParameters: Map<String, String>? = null,
        val ResourceId: String,
        val RestApiId: String
    ) : ResourceProperties {

        data class IntegrationProperty(
            val CacheKeyParameters: List<String>? = null,
            val CacheNamespace: String? = null,
            val Credentials: String? = null,
            val IntegrationHttpMethod: String? = null,
            val IntegrationResponses: List<ApiGateway.Method.IntegrationProperty.IntegrationResponseProperty>? = null,
            val PassthroughBehavior: String? = null,
            val RequestParameters: Map<String, String>? = null,
            val RequestTemplates: Map<String, String>? = null,
            val Type: String,
            val Uri: String? = null
        ) {

            data class IntegrationResponseProperty(
                val ResponseParameters: Map<String, String>? = null,
                val ResponseTemplates: Map<String, String>? = null,
                val SelectionPattern: String? = null,
                val StatusCode: String? = null
            ) 

        }


        data class MethodResponseProperty(
            val ResponseModels: Map<String, String>? = null,
            val ResponseParameters: Map<String, String>? = null,
            val StatusCode: String
        ) 

    }

    @CloudFormationType("AWS::ApiGateway::Model")
    data class Model(
        val ContentType: String? = null,
        val Description: String? = null,
        val Name: String? = null,
        val RestApiId: String,
        val Schema: Any
    ) : ResourceProperties 

    @CloudFormationType("AWS::ApiGateway::Resource")
    data class Resource(
        val ParentId: String,
        val PathPart: String,
        val RestApiId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::ApiGateway::RestApi")
    data class RestApi(
        val Body: Any? = null,
        val BodyS3Location: RestApi.S3LocationProperty? = null,
        val CloneFrom: String? = null,
        val Description: String? = null,
        val FailOnWarnings: String? = null,
        val Name: String? = null,
        val Parameters: List<String>? = null
    ) : ResourceProperties {

        data class S3LocationProperty(
            val Bucket: String? = null,
            val ETag: String? = null,
            val Key: String? = null,
            val Version: String? = null
        ) 

    }

    @CloudFormationType("AWS::ApiGateway::Stage")
    data class Stage(
        val CacheClusterEnabled: String? = null,
        val CacheClusterSize: String? = null,
        val ClientCertificateId: String? = null,
        val DeploymentId: String,
        val Description: String? = null,
        val MethodSettings: List<ApiGateway.Stage.MethodSettingProperty>? = null,
        val RestApiId: String,
        val StageName: String,
        val Variables: Map<String, String>? = null
    ) : ResourceProperties {

        data class MethodSettingProperty(
            val CacheDataEncrypted: String? = null,
            val CacheTtlInSeconds: String? = null,
            val CachingEnabled: String? = null,
            val DataTraceEnabled: String? = null,
            val HttpMethod: String,
            val LoggingLevel: String? = null,
            val MetricsEnabled: String? = null,
            val ResourcePath: String,
            val ThrottlingBurstLimit: String? = null,
            val ThrottlingRateLimit: String? = null
        ) 

    }

    @CloudFormationType("AWS::ApiGateway::UsagePlan")
    data class UsagePlan(
        val ApiStages: List<ApiGateway.UsagePlan.ApiStageProperty>? = null,
        val Description: String? = null,
        val Quota: UsagePlan.QuotaSettingProperty? = null,
        val Throttle: UsagePlan.ThrottleSettingProperty? = null,
        val UsagePlanName: String? = null
    ) : ResourceProperties {

        data class ApiStageProperty(
            val ApiId: String? = null,
            val Stage: String? = null
        ) 


        data class QuotaSettingProperty(
            val Limit: String? = null,
            val Offset: String? = null,
            val Period: String? = null
        ) 


        data class ThrottleSettingProperty(
            val BurstLimit: String? = null,
            val RateLimit: String? = null
        ) 

    }


}