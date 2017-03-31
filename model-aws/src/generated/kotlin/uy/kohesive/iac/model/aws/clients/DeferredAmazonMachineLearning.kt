package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.machinelearning.AbstractAmazonMachineLearning
import com.amazonaws.services.machinelearning.AmazonMachineLearning
import com.amazonaws.services.machinelearning.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonMachineLearning(val context: IacContext) : AbstractAmazonMachineLearning(), AmazonMachineLearning {


}

class DeferredAmazonMachineLearning(context: IacContext) : BaseDeferredAmazonMachineLearning(context)
