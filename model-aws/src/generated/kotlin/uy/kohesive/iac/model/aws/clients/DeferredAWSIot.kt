package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.iot.AbstractAWSIot
import com.amazonaws.services.iot.AWSIot
import com.amazonaws.services.iot.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSIot(val context: IacContext) : AbstractAWSIot(), AWSIot {


}

class DeferredAWSIot(context: IacContext) : BaseDeferredAWSIot(context)
