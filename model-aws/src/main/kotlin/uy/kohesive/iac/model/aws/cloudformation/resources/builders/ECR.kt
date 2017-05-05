package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.ecr.model.CreateRepositoryRequest
import com.amazonaws.services.ecr.model.SetRepositoryPolicyRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.ECR
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class ECRRepositoryResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateRepositoryRequest> {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    override val requestClazz = CreateRepositoryRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateRepositoryRequest).let {
            ECR.Repository(
                RepositoryName       = request.repositoryName,
                RepositoryPolicyText = relatedObjects.filterIsInstance<SetRepositoryPolicyRequest>().firstOrNull()?.let {
                    it.policyText?.let { policyJSON ->
                        JSON.readValue<Map<String, Any>>(policyJSON)
                    }
                }
            )
        }

}

