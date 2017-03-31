package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.logs.AWSLogs
import com.amazonaws.services.logs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface LogsIdentifiable : KohesiveIdentifiable {

}

interface LogsEnabled : LogsIdentifiable {
    val logsClient: AWSLogs
    val logsContext: LogsContext
    fun <T> withLogsContext(init: LogsContext.(AWSLogs) -> T): T = logsContext.init(logsClient)
}

open class BaseLogsContext(protected val context: IacContext) : LogsEnabled by context {

    fun createLogGroup(logGroupName: String, init: CreateLogGroupRequest.() -> Unit): CreateLogGroupResult {
        return logsClient.createLogGroup(CreateLogGroupRequest().apply {
            withLogGroupName(logGroupName)
            init()
        })
    }

    fun createLogStream(logStreamName: String, init: CreateLogStreamRequest.() -> Unit): CreateLogStreamResult {
        return logsClient.createLogStream(CreateLogStreamRequest().apply {
            withLogStreamName(logStreamName)
            init()
        })
    }


}

@DslScope
class LogsContext(context: IacContext) : BaseLogsContext(context) {

}
