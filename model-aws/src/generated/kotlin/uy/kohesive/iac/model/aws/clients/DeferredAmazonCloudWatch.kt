package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudwatch.AbstractAmazonCloudWatch
import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatch.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonCloudWatch(val context: IacContext) : AbstractAmazonCloudWatch(), AmazonCloudWatch {


}

class DeferredAmazonCloudWatch(context: IacContext) : BaseDeferredAmazonCloudWatch(context)
