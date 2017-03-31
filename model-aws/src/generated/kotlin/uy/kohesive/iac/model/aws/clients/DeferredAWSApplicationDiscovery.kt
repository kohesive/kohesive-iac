package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.applicationdiscovery.AWSApplicationDiscovery
import com.amazonaws.services.applicationdiscovery.AbstractAWSApplicationDiscovery
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSApplicationDiscovery(val context: IacContext) : AbstractAWSApplicationDiscovery(), AWSApplicationDiscovery {


}

class DeferredAWSApplicationDiscovery(context: IacContext) : BaseDeferredAWSApplicationDiscovery(context)
