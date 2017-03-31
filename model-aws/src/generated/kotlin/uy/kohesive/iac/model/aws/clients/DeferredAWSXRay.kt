package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.xray.AbstractAWSXRay
import com.amazonaws.services.xray.AWSXRay
import com.amazonaws.services.xray.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSXRay(val context: IacContext) : AbstractAWSXRay(), AWSXRay {


}

class DeferredAWSXRay(context: IacContext) : BaseDeferredAWSXRay(context)
