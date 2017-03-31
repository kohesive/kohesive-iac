package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.logs.AbstractAWSLogs
import com.amazonaws.services.logs.AWSLogs
import com.amazonaws.services.logs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSLogs(val context: IacContext) : AbstractAWSLogs(), AWSLogs {

    override fun createExportTask(request: CreateExportTaskRequest): CreateExportTaskResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateExportTaskRequest, CreateExportTaskResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createLogGroup(request: CreateLogGroupRequest): CreateLogGroupResult {
        return with (context) {
            request.registerWithAutoName()
            CreateLogGroupResult().registerWithSameNameAs(request)
        }
    }

    override fun createLogStream(request: CreateLogStreamRequest): CreateLogStreamResult {
        return with (context) {
            request.registerWithAutoName()
            CreateLogStreamResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSLogs(context: IacContext) : BaseDeferredAWSLogs(context)
