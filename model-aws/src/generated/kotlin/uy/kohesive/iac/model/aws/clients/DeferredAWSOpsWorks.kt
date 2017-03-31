package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.opsworks.AbstractAWSOpsWorks
import com.amazonaws.services.opsworks.AWSOpsWorks
import com.amazonaws.services.opsworks.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSOpsWorks(val context: IacContext) : AbstractAWSOpsWorks(), AWSOpsWorks {


}

class DeferredAWSOpsWorks(context: IacContext) : BaseDeferredAWSOpsWorks(context)
