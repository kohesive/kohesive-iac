package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.mturk.AbstractAmazonMTurk
import com.amazonaws.services.mturk.AmazonMTurk
import com.amazonaws.services.mturk.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonMTurk(val context: IacContext) : AbstractAmazonMTurk(), AmazonMTurk {

}

class DeferredAmazonMTurk(context: IacContext) : BaseDeferredAmazonMTurk(context)