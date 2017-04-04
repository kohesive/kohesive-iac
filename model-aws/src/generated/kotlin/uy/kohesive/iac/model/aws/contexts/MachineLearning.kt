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

    open fun createBatchPrediction(batchPredictionName: String, init: CreateBatchPredictionRequest.() -> Unit): CreateBatchPredictionResult {
        return machineLearningClient.createBatchPrediction(CreateBatchPredictionRequest().apply {
            withBatchPredictionName(batchPredictionName)
            init()
        })
    }

    open fun createEvaluation(evaluationName: String, init: CreateEvaluationRequest.() -> Unit): CreateEvaluationResult {
        return machineLearningClient.createEvaluation(CreateEvaluationRequest().apply {
            withEvaluationName(evaluationName)
            init()
        })
    }

    open fun createMLModel(mLModelName: String, init: CreateMLModelRequest.() -> Unit): CreateMLModelResult {
        return machineLearningClient.createMLModel(CreateMLModelRequest().apply {
            withMLModelName(mLModelName)
            init()
        })
    }


}

@DslScope
class MachineLearningContext(context: IacContext) : BaseMachineLearningContext(context) {

}
