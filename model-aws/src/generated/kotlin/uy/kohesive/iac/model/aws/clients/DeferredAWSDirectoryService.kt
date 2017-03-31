package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.directory.AbstractAWSDirectoryService
import com.amazonaws.services.directory.AWSDirectoryService
import com.amazonaws.services.directory.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSDirectoryService(val context: IacContext) : AbstractAWSDirectoryService(), AWSDirectoryService {

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


}

class DeferredAWSDirectoryService(context: IacContext) : BaseDeferredAWSDirectoryService(context)
