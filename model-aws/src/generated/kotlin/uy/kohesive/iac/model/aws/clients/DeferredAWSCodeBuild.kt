package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.codebuild.AbstractAWSCodeBuild
import com.amazonaws.services.codebuild.AWSCodeBuild
import com.amazonaws.services.codebuild.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCodeBuild(val context: IacContext) : AbstractAWSCodeBuild(), AWSCodeBuild {

    override fun createProject(request: CreateProjectRequest): CreateProjectResult {
        return with (context) {
            request.registerWithAutoName()
            CreateProjectResult().withProject(
                makeProxy<CreateProjectRequest, Project>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateProjectRequest::getName to Project::getName,
                        CreateProjectRequest::getDescription to Project::getDescription,
                        CreateProjectRequest::getSource to Project::getSource,
                        CreateProjectRequest::getArtifacts to Project::getArtifacts,
                        CreateProjectRequest::getEnvironment to Project::getEnvironment,
                        CreateProjectRequest::getServiceRole to Project::getServiceRole,
                        CreateProjectRequest::getTimeoutInMinutes to Project::getTimeoutInMinutes,
                        CreateProjectRequest::getEncryptionKey to Project::getEncryptionKey,
                        CreateProjectRequest::getTags to Project::getTags
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSCodeBuild(context: IacContext) : BaseDeferredAWSCodeBuild(context)
