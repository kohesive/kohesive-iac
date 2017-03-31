package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.polly.AbstractAmazonPolly
import com.amazonaws.services.polly.AmazonPolly
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonPolly(val context: IacContext) : AbstractAmazonPolly(), AmazonPolly {


}

class DeferredAmazonPolly(context: IacContext) : BaseDeferredAmazonPolly(context)
