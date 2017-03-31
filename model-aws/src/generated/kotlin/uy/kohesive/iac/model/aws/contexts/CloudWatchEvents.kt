package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEvents
import com.amazonaws.services.cloudwatchevents.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CloudWatchEventsIdentifiable : KohesiveIdentifiable {

}

interface CloudWatchEventsEnabled : CloudWatchEventsIdentifiable {
    val cloudWatchEventsClient: AmazonCloudWatchEvents
    val cloudWatchEventsContext: CloudWatchEventsContext
    fun <T> withCloudWatchEventsContext(init: CloudWatchEventsContext.(AmazonCloudWatchEvents) -> T): T = cloudWatchEventsContext.init(cloudWatchEventsClient)
}

open class BaseCloudWatchEventsContext(protected val context: IacContext) : CloudWatchEventsEnabled by context {


}

@DslScope
class CloudWatchEventsContext(context: IacContext) : BaseCloudWatchEventsContext(context) {

}
