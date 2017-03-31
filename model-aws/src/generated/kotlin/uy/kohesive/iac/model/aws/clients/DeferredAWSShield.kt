package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.shield.AbstractAWSShield
import com.amazonaws.services.shield.AWSShield
import com.amazonaws.services.shield.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSShield(val context: IacContext) : AbstractAWSShield(), AWSShield {


}

class DeferredAWSShield(context: IacContext) : BaseDeferredAWSShield(context)
