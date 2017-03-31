package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudwatch.AbstractAmazonCloudWatch
import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonCloudWatch(val context: IacContext) : AbstractAmazonCloudWatch(), AmazonCloudWatch {


}

class DeferredAmazonCloudWatch(context: IacContext) : BaseDeferredAmazonCloudWatch(context)
