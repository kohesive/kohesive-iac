package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatch.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CloudWatchIdentifiable : KohesiveIdentifiable {

}

interface CloudWatchEnabled : CloudWatchIdentifiable {
    val cloudWatchClient: AmazonCloudWatch
    val cloudWatchContext: CloudWatchContext
    fun <T> withCloudWatchContext(init: CloudWatchContext.(AmazonCloudWatch) -> T): T = cloudWatchContext.init(cloudWatchClient)
}

open class BaseCloudWatchContext(protected val context: IacContext) : CloudWatchEnabled by context {


}

@DslScope
class CloudWatchContext(context: IacContext) : BaseCloudWatchContext(context) {

}
