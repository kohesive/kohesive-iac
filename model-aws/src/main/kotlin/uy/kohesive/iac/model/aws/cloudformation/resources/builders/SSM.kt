package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.simplesystemsmanagement.model.CreateAssociationRequest
import com.amazonaws.services.simplesystemsmanagement.model.CreateDocumentRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.SSM

class SSMAssociationResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateAssociationRequest> {

    override val requestClazz = CreateAssociationRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAssociationRequest).let {
            SSM.Association(
                DocumentVersion    = request.documentVersion,
                InstanceId         = request.instanceId,
                Name               = request.name,
                Parameters         = request.parameters,
                ScheduleExpression = request.scheduleExpression,
                Targets            = request.targets?.map {
                    SSM.Association.TargetProperty(
                        Key    = it.key,
                        Values = it.values
                    )
                }
            )
        }

}

class SSMDocumentResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDocumentRequest> {

    override val requestClazz = CreateDocumentRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDocumentRequest).let {
            SSM.Document(
                Content      = request.content,
                DocumentType = request.documentType
            )
        }

}

