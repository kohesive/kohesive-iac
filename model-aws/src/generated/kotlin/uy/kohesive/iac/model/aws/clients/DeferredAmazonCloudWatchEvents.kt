package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudwatchevents.AbstractAmazonCloudWatchEvents
import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEvents
import com.amazonaws.services.cloudwatchevents.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonCloudWatchEvents(val context: IacContext) : AbstractAmazonCloudWatchEvents(), AmazonCloudWatchEvents {


}

class DeferredAmazonCloudWatchEvents(context: IacContext) : BaseDeferredAmazonCloudWatchEvents(context)
