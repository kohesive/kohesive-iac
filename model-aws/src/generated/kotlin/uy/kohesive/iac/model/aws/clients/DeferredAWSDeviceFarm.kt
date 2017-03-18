package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.devicefarm.AbstractAWSDeviceFarm
import com.amazonaws.services.devicefarm.AWSDeviceFarm
import com.amazonaws.services.devicefarm.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSDeviceFarm(val context: IacContext) : AbstractAWSDeviceFarm(), AWSDeviceFarm {

}

class DeferredAWSDeviceFarm(context: IacContext) : BaseDeferredAWSDeviceFarm(context)