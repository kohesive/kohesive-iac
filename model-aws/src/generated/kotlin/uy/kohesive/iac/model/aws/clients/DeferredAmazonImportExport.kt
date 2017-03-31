package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.importexport.AbstractAmazonImportExport
import com.amazonaws.services.importexport.AmazonImportExport
import com.amazonaws.services.importexport.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonImportExport(val context: IacContext) : AbstractAmazonImportExport(), AmazonImportExport {

    override fun createJob(request: CreateJobRequest): CreateJobResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateJobRequest, CreateJobResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateJobRequest::getJobType to CreateJobResult::getJobType
                )
            )
        }
    }


}

class DeferredAmazonImportExport(context: IacContext) : BaseDeferredAmazonImportExport(context)
