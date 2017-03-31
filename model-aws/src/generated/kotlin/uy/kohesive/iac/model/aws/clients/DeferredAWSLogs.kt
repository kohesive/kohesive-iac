package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.logs.AbstractAWSLogs
import com.amazonaws.services.logs.AWSLogs
import com.amazonaws.services.logs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSLogs(val context: IacContext) : AbstractAWSLogs(), AWSLogs {


}

class DeferredAWSLogs(context: IacContext) : BaseDeferredAWSLogs(context)
