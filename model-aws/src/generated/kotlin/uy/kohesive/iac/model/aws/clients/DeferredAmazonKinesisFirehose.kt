package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.kinesisfirehose.AbstractAmazonKinesisFirehose
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose
import com.amazonaws.services.kinesisfirehose.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonKinesisFirehose(val context: IacContext) : AbstractAmazonKinesisFirehose(), AmazonKinesisFirehose {

}

class DeferredAmazonKinesisFirehose(context: IacContext) : BaseDeferredAmazonKinesisFirehose(context)