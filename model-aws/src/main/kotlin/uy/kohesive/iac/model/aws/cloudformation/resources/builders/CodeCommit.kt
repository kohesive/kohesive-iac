package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.codecommit.model.CreateRepositoryRequest
import com.amazonaws.services.codecommit.model.PutRepositoryTriggersRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CodeCommit

class CodeCommitRepositoryResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateRepositoryRequest> {

    override val requestClazz = CreateRepositoryRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateRepositoryRequest).let {
            CodeCommit.Repository(
                RepositoryDescription = request.repositoryDescription,
                RepositoryName        = request.repositoryName,
                Triggers              = relatedObjects.filterIsInstance<PutRepositoryTriggersRequest>().flatMap { it.triggers }.map {
                    CodeCommit.Repository.TriggerProperty(
                        Branches       = it.branches,
                        Name           = it.name,
                        CustomData     = it.customData,
                        DestinationArn = it.destinationArn,
                        Events         = it.events
                    )
                }
            )
        }

}

