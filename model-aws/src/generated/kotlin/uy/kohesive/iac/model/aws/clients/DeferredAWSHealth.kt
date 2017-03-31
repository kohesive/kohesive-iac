package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.health.AbstractAWSHealth
import com.amazonaws.services.health.AWSHealth
import com.amazonaws.services.health.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSHealth(val context: IacContext) : AbstractAWSHealth(), AWSHealth {


}

class DeferredAWSHealth(context: IacContext) : BaseDeferredAWSHealth(context)
