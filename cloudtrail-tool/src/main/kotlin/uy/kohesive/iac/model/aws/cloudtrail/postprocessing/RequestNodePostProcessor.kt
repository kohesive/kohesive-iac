package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode

interface RequestNodePostProcessor {

    val shapeNames: List<String>

    fun process(requestMapNode: RequestMapNode, awsModel: IntermediateModel): RequestMapNode

}

object RequestPostProcessors {

    private val postProcessors: List<RequestNodePostProcessor> = listOf(
        CreateBucketProcessor(),
        SetBucketAclProcessor(),
        SetBucketPolicyProcessor(),
        SetBucketLoggingConfigurationProcessor(),
        SetBucketWebsiteConfigurationProcessor(),
        SetBucketTaggingConfigurationProcessor()
    )

    private val shapeNameToPostProcessors = postProcessors.flatMap { postprocessor ->
        postprocessor.shapeNames.map { shapeName ->
            shapeName to postprocessor
        }
    }.groupBy { it.first }.mapValues { it.value.map { it.second } }

    fun postProcess(requestMapNode: RequestMapNode, awsModel: IntermediateModel): RequestMapNode {
        val shapeName      = requestMapNode.shape?.c2jName ?: throw IllegalArgumentException("Can't process a node with a null shape")
        val postProcessors = shapeNameToPostProcessors[shapeName]

        return postProcessors?.fold(requestMapNode) { currentRequest, preprocessor ->
            preprocessor.process(currentRequest, awsModel)
        } ?: requestMapNode
    }

}