package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.gamelift.AbstractAmazonGameLift
import com.amazonaws.services.gamelift.AmazonGameLift
import com.amazonaws.services.gamelift.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonGameLift(val context: IacContext) : AbstractAmazonGameLift(), AmazonGameLift {

}

class DeferredAmazonGameLift(context: IacContext) : BaseDeferredAmazonGameLift(context)