package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.kinesisanalytics.AbstractAmazonKinesisAnalytics
import com.amazonaws.services.kinesisanalytics.AmazonKinesisAnalytics
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonKinesisAnalytics(val context: IacContext) : AbstractAmazonKinesisAnalytics(), AmazonKinesisAnalytics {


}

class DeferredAmazonKinesisAnalytics(context: IacContext) : BaseDeferredAmazonKinesisAnalytics(context)
