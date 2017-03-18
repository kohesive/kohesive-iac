package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.directconnect.AbstractAmazonDirectConnect
import com.amazonaws.services.directconnect.AmazonDirectConnect
import com.amazonaws.services.directconnect.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonDirectConnect(val context: IacContext) : AbstractAmazonDirectConnect(), AmazonDirectConnect {

}

class DeferredAmazonDirectConnect(context: IacContext) : BaseDeferredAmazonDirectConnect(context)