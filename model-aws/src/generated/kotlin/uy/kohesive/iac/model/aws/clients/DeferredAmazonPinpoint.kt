package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.pinpoint.AbstractAmazonPinpoint
import com.amazonaws.services.pinpoint.AmazonPinpoint
import com.amazonaws.services.pinpoint.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonPinpoint(val context: IacContext) : AbstractAmazonPinpoint(), AmazonPinpoint {

}

class DeferredAmazonPinpoint(context: IacContext) : BaseDeferredAmazonPinpoint(context)