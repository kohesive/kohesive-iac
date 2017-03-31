package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudhsm.AbstractAWSCloudHSM
import com.amazonaws.services.cloudhsm.AWSCloudHSM
import com.amazonaws.services.cloudhsm.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCloudHSM(val context: IacContext) : AbstractAWSCloudHSM(), AWSCloudHSM {


}

class DeferredAWSCloudHSM(context: IacContext) : BaseDeferredAWSCloudHSM(context)
