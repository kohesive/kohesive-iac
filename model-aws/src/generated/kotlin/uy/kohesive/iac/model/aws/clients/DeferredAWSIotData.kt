package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.iotdata.AbstractAWSIotData
import com.amazonaws.services.iotdata.AWSIotData
import com.amazonaws.services.iotdata.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSIotData(val context: IacContext) : AbstractAWSIotData(), AWSIotData {


}

class DeferredAWSIotData(context: IacContext) : BaseDeferredAWSIotData(context)
