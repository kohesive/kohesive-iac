package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.elasticfilesystem.model.CreateFileSystemRequest
import com.amazonaws.services.elasticfilesystem.model.CreateMountTargetRequest
import com.amazonaws.services.elasticfilesystem.model.CreateTagsRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.EFS

class EFSFileSystemResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateFileSystemRequest> {

    override val requestClazz = CreateFileSystemRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateFileSystemRequest).let {
            EFS.FileSystem(
                PerformanceMode = request.performanceMode,
                FileSystemTags  = relatedObjects.filterIsInstance<CreateTagsRequest>().flatMap { it.tags }.map {
                    EFS.FileSystem.FileSystemTagProperty(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

class EFSMountTargetResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateMountTargetRequest> {

    override val requestClazz = CreateMountTargetRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateMountTargetRequest).let {
            EFS.MountTarget(
                FileSystemId   = request.fileSystemId,
                IpAddress      = request.ipAddress,
                SecurityGroups = request.securityGroups,
                SubnetId       = request.subnetId
            )
        }

}

