package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.storagegateway.AbstractAWSStorageGateway
import com.amazonaws.services.storagegateway.AWSStorageGateway
import com.amazonaws.services.storagegateway.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSStorageGateway(val context: IacContext) : AbstractAWSStorageGateway(), AWSStorageGateway {

    override fun addCache(request: AddCacheRequest): AddCacheResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddCacheRequest, AddCacheResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    AddCacheRequest::getGatewayARN to AddCacheResult::getGatewayARN
                )
            )
        }
    }

    override fun addTagsToResource(request: AddTagsToResourceRequest): AddTagsToResourceResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddTagsToResourceRequest, AddTagsToResourceResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    AddTagsToResourceRequest::getResourceARN to AddTagsToResourceResult::getResourceARN
                )
            )
        }
    }

    override fun addUploadBuffer(request: AddUploadBufferRequest): AddUploadBufferResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddUploadBufferRequest, AddUploadBufferResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    AddUploadBufferRequest::getGatewayARN to AddUploadBufferResult::getGatewayARN
                )
            )
        }
    }

    override fun addWorkingStorage(request: AddWorkingStorageRequest): AddWorkingStorageResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddWorkingStorageRequest, AddWorkingStorageResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    AddWorkingStorageRequest::getGatewayARN to AddWorkingStorageResult::getGatewayARN
                )
            )
        }
    }

    override fun createCachediSCSIVolume(request: CreateCachediSCSIVolumeRequest): CreateCachediSCSIVolumeResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateCachediSCSIVolumeRequest, CreateCachediSCSIVolumeResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createNFSFileShare(request: CreateNFSFileShareRequest): CreateNFSFileShareResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateNFSFileShareRequest, CreateNFSFileShareResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createSnapshot(request: CreateSnapshotRequest): CreateSnapshotResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateSnapshotRequest, CreateSnapshotResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateSnapshotRequest::getVolumeARN to CreateSnapshotResult::getVolumeARN
                )
            )
        }
    }

    override fun createSnapshotFromVolumeRecoveryPoint(request: CreateSnapshotFromVolumeRecoveryPointRequest): CreateSnapshotFromVolumeRecoveryPointResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateSnapshotFromVolumeRecoveryPointRequest, CreateSnapshotFromVolumeRecoveryPointResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateSnapshotFromVolumeRecoveryPointRequest::getVolumeARN to CreateSnapshotFromVolumeRecoveryPointResult::getVolumeARN
                )
            )
        }
    }

    override fun createStorediSCSIVolume(request: CreateStorediSCSIVolumeRequest): CreateStorediSCSIVolumeResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateStorediSCSIVolumeRequest, CreateStorediSCSIVolumeResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createTapeWithBarcode(request: CreateTapeWithBarcodeRequest): CreateTapeWithBarcodeResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateTapeWithBarcodeRequest, CreateTapeWithBarcodeResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createTapes(request: CreateTapesRequest): CreateTapesResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateTapesRequest, CreateTapesResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSStorageGateway(context: IacContext) : BaseDeferredAWSStorageGateway(context)
