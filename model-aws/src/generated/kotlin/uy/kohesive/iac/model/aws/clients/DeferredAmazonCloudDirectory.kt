package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.clouddirectory.AbstractAmazonCloudDirectory
import com.amazonaws.services.clouddirectory.AmazonCloudDirectory
import com.amazonaws.services.clouddirectory.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonCloudDirectory(val context: IacContext) : AbstractAmazonCloudDirectory(), AmazonCloudDirectory {

    override fun addFacetToObject(request: AddFacetToObjectRequest): AddFacetToObjectResult {
        return with (context) {
            request.registerWithAutoName()
            AddFacetToObjectResult().registerWithSameNameAs(request)
        }
    }

    override fun attachObject(request: AttachObjectRequest): AttachObjectResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AttachObjectRequest, AttachObjectResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun attachPolicy(request: AttachPolicyRequest): AttachPolicyResult {
        return with (context) {
            request.registerWithAutoName()
            AttachPolicyResult().registerWithSameNameAs(request)
        }
    }

    override fun attachToIndex(request: AttachToIndexRequest): AttachToIndexResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AttachToIndexRequest, AttachToIndexResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createDirectory(request: CreateDirectoryRequest): CreateDirectoryResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDirectoryRequest, CreateDirectoryResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDirectoryRequest::getName to CreateDirectoryResult::getName
                )
            )
        }
    }

    override fun createFacet(request: CreateFacetRequest): CreateFacetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateFacetResult().registerWithSameNameAs(request)
        }
    }

    override fun createIndex(request: CreateIndexRequest): CreateIndexResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateIndexRequest, CreateIndexResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createObject(request: CreateObjectRequest): CreateObjectResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateObjectRequest, CreateObjectResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createSchema(request: CreateSchemaRequest): CreateSchemaResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateSchemaRequest, CreateSchemaResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonCloudDirectory(context: IacContext) : BaseDeferredAmazonCloudDirectory(context)
