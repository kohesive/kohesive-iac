package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.kinesis.AbstractAmazonKinesis
import com.amazonaws.services.kinesis.AmazonKinesis
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonKinesis(val context: IacContext) : AbstractAmazonKinesis(), AmazonKinesis {


}

class DeferredAmazonKinesis(context: IacContext) : BaseDeferredAmazonKinesis(context)
