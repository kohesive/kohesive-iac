package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.batch.AbstractAWSBatch
import com.amazonaws.services.batch.AWSBatch
import com.amazonaws.services.batch.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSBatch(val context: IacContext) : AbstractAWSBatch(), AWSBatch {

    override fun createComputeEnvironment(request: CreateComputeEnvironmentRequest): CreateComputeEnvironmentResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateComputeEnvironmentRequest, CreateComputeEnvironmentResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateComputeEnvironmentRequest::getComputeEnvironmentName to CreateComputeEnvironmentResult::getComputeEnvironmentName
                )
            )
        }
    }

    override fun createJobQueue(request: CreateJobQueueRequest): CreateJobQueueResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateJobQueueRequest, CreateJobQueueResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateJobQueueRequest::getJobQueueName to CreateJobQueueResult::getJobQueueName
                )
            )
        }
    }


}

class DeferredAWSBatch(context: IacContext) : BaseDeferredAWSBatch(context)
