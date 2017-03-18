package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.machinelearning.AmazonMachineLearning
import com.amazonaws.services.machinelearning.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface MachineLearningIdentifiable : KohesiveIdentifiable {

}

interface MachineLearningEnabled : MachineLearningIdentifiable {
    val machineLearningClient: AmazonMachineLearning
    val machineLearningContext: MachineLearningContext
    fun <T> withMachineLearningContext(init: MachineLearningContext.(AmazonMachineLearning) -> T): T = machineLearningContext.init(machineLearningClient)
}

open class BaseMachineLearningContext(protected val context: IacContext) : MachineLearningEnabled by context {

}

@DslScope
class MachineLearningContext(context: IacContext) : BaseMachineLearningContext(context) {

}