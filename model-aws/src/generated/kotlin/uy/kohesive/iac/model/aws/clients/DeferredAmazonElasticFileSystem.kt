package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticfilesystem.AbstractAmazonElasticFileSystem
import com.amazonaws.services.elasticfilesystem.AmazonElasticFileSystem
import com.amazonaws.services.elasticfilesystem.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonElasticFileSystem(val context: IacContext) : AbstractAmazonElasticFileSystem(), AmazonElasticFileSystem {

    override fun createFileSystem(request: CreateFileSystemRequest): CreateFileSystemResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateFileSystemRequest, CreateFileSystemResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateFileSystemRequest::getCreationToken to CreateFileSystemResult::getCreationToken,
                    CreateFileSystemRequest::getPerformanceMode to CreateFileSystemResult::getPerformanceMode
                )
            )
        }
    }

    override fun createMountTarget(request: CreateMountTargetRequest): CreateMountTargetResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateMountTargetRequest, CreateMountTargetResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateMountTargetRequest::getFileSystemId to CreateMountTargetResult::getFileSystemId,
                    CreateMountTargetRequest::getSubnetId to CreateMountTargetResult::getSubnetId,
                    CreateMountTargetRequest::getIpAddress to CreateMountTargetResult::getIpAddress
                )
            )
        }
    }

    override fun createTags(request: CreateTagsRequest): CreateTagsResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTagsResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonElasticFileSystem(context: IacContext) : BaseDeferredAmazonElasticFileSystem(context)
