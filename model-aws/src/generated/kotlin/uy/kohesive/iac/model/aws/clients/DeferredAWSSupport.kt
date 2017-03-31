package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.support.AbstractAWSSupport
import com.amazonaws.services.support.AWSSupport
import com.amazonaws.services.support.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSSupport(val context: IacContext) : AbstractAWSSupport(), AWSSupport {


}

class DeferredAWSSupport(context: IacContext) : BaseDeferredAWSSupport(context)
