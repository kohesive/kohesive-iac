package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.simpledb.model.CreateDomainRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.SDB

class SDBDomainResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDomainRequest> {

    override val requestClazz = CreateDomainRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDomainRequest).let {
            SDB.Domain()
        }

}

