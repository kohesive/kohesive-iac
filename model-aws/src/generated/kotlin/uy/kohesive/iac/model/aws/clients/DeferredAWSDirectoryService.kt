package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.directory.AbstractAWSDirectoryService
import com.amazonaws.services.directory.AWSDirectoryService
import com.amazonaws.services.directory.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSDirectoryService(val context: IacContext) : AbstractAWSDirectoryService(), AWSDirectoryService {

    override fun addIpRoutes(request: AddIpRoutesRequest): AddIpRoutesResult {
        return with (context) {
            request.registerWithAutoName()
            AddIpRoutesResult().registerWithSameNameAs(request)
        }
    }

    override fun addTagsToResource(request: AddTagsToResourceRequest): AddTagsToResourceResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsToResourceResult().registerWithSameNameAs(request)
        }
    }

    override fun createAlias(request: CreateAliasRequest): CreateAliasResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateAliasRequest, CreateAliasResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateAliasRequest::getDirectoryId to CreateAliasResult::getDirectoryId,
                    CreateAliasRequest::getAlias to CreateAliasResult::getAlias
                )
            )
        }
    }

    override fun createComputer(request: CreateComputerRequest): CreateComputerResult {
        return with (context) {
            request.registerWithAutoName()
            CreateComputerResult().withComputer(
                makeProxy<CreateComputerRequest, Computer>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateComputerRequest::getComputerName to Computer::getComputerName,
                        CreateComputerRequest::getComputerAttributes to Computer::getComputerAttributes
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createConditionalForwarder(request: CreateConditionalForwarderRequest): CreateConditionalForwarderResult {
        return with (context) {
            request.registerWithAutoName()
            CreateConditionalForwarderResult().registerWithSameNameAs(request)
        }
    }

    override fun createDirectory(request: CreateDirectoryRequest): CreateDirectoryResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDirectoryRequest, CreateDirectoryResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createMicrosoftAD(request: CreateMicrosoftADRequest): CreateMicrosoftADResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateMicrosoftADRequest, CreateMicrosoftADResult>(
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
                requestObject = request
            )
        }
    }

    override fun createTrust(request: CreateTrustRequest): CreateTrustResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateTrustRequest, CreateTrustResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSDirectoryService(context: IacContext) : BaseDeferredAWSDirectoryService(context)
