package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.kinesisanalytics.AmazonKinesisAnalytics
import com.amazonaws.services.kinesisanalytics.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface KinesisAnalyticsIdentifiable : KohesiveIdentifiable {

}

interface KinesisAnalyticsEnabled : KinesisAnalyticsIdentifiable {
    val kinesisAnalyticsClient: AmazonKinesisAnalytics
    val kinesisAnalyticsContext: KinesisAnalyticsContext
    fun <T> withKinesisAnalyticsContext(init: KinesisAnalyticsContext.(AmazonKinesisAnalytics) -> T): T = kinesisAnalyticsContext.init(kinesisAnalyticsClient)
}

open class BaseKinesisAnalyticsContext(protected val context: IacContext) : KinesisAnalyticsEnabled by context {

    open fun createApplication(applicationName: String, init: CreateApplicationRequest.() -> Unit): CreateApplicationResult {
        return kinesisAnalyticsClient.createApplication(CreateApplicationRequest().apply {
            withApplicationName(applicationName)
            init()
        })
    }


}

@DslScope
class KinesisAnalyticsContext(context: IacContext) : BaseKinesisAnalyticsContext(context) {

}
