package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.kms.model.CreateAliasRequest
import com.amazonaws.services.kms.model.CreateKeyRequest
import com.amazonaws.services.kms.model.EnableKeyRequest
import com.amazonaws.services.kms.model.EnableKeyRotationRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.KMS
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class KMSAliasResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateAliasRequest> {

    override val requestClazz = CreateAliasRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAliasRequest).let {
            KMS.Alias(
                AliasName   = request.aliasName,
                TargetKeyId = request.targetKeyId
            )
        }

}

class KMSKeyResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateKeyRequest> {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    override val requestClazz = CreateKeyRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateKeyRequest).let {
            KMS.Key(
                Description       = request.description,
                EnableKeyRotation = relatedObjects.filterIsInstance<EnableKeyRotationRequest>().isNotEmpty().toString(),
                Enabled           = relatedObjects.filterIsInstance<EnableKeyRequest>().isNotEmpty().toString(),
                KeyPolicy         = request.policy.let {
                    JSON.readValue<Map<String, Any>>(it)
                }
            )
        }

}

