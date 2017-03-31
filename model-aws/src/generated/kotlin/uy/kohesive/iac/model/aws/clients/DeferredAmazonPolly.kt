package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.polly.AbstractAmazonPolly
import com.amazonaws.services.polly.AmazonPolly
import com.amazonaws.services.polly.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonPolly(val context: IacContext) : AbstractAmazonPolly(), AmazonPolly {


}

class DeferredAmazonPolly(context: IacContext) : BaseDeferredAmazonPolly(context)
