package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.sns.AbstractAmazonSNS
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonSNS(val context: IacContext) : AbstractAmazonSNS(), AmazonSNS {

}

class DeferredAmazonSNS(context: IacContext) : BaseDeferredAmazonSNS(context)