package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.applicationautoscaling.AbstractAWSApplicationAutoScaling
import com.amazonaws.services.applicationautoscaling.AWSApplicationAutoScaling
import com.amazonaws.services.applicationautoscaling.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSApplicationAutoScaling(val context: IacContext) : AbstractAWSApplicationAutoScaling(), AWSApplicationAutoScaling {


}

class DeferredAWSApplicationAutoScaling(context: IacContext) : BaseDeferredAWSApplicationAutoScaling(context)
