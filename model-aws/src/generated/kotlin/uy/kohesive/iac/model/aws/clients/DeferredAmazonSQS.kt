package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.sqs.AbstractAmazonSQS
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonSQS(val context: IacContext) : AbstractAmazonSQS(), AmazonSQS {


}

class DeferredAmazonSQS(context: IacContext) : BaseDeferredAmazonSQS(context)
