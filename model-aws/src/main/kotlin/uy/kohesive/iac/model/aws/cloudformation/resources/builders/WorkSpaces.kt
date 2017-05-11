package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.workspaces.model.CreateWorkspacesRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.WorkSpaces
import java.lang.IllegalArgumentException

class WorkSpacesWorkspaceResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateWorkspacesRequest> {

    override val requestClazz = CreateWorkspacesRequest::class

    override fun canBuildFrom(request: AmazonWebServiceRequest) = (request as CreateWorkspacesRequest).workspaces.isNotEmpty()

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateWorkspacesRequest).let {
            if (request.workspaces.size > 1) {
                // TODO: deferred client must throw this too
                throw IllegalArgumentException("Creating multiple workspaces in one request not supported, use one CreateWorkspacesRequest per workspace")
            }
            val workspaceRequest = request.workspaces.first()

            WorkSpaces.Workspace(
                BundleId                    = workspaceRequest.bundleId,
                DirectoryId                 = workspaceRequest.directoryId,
                RootVolumeEncryptionEnabled = workspaceRequest.rootVolumeEncryptionEnabled?.toString(),
                UserName                    = workspaceRequest.userName,
                UserVolumeEncryptionEnabled = workspaceRequest.userVolumeEncryptionEnabled?.toString(),
                VolumeEncryptionKey         = workspaceRequest.volumeEncryptionKey
            )
        }

}

