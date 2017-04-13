package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode

interface RequestNodePostProcessor {

    val shapeNames: List<String>

    fun process(requestMapNode: RequestMapNode): RequestMapNode

}

object RequestPostProcessors {

    private val postProcessors: List<RequestNodePostProcessor> = listOf(
        CreateBucketProcessor()
    )

    private val shapeNameToPostProcessors = postProcessors.flatMap { postprocessor ->
        postprocessor.shapeNames.map { shapeName ->
            shapeName to postprocessor
        }
    }.groupBy { it.first }.mapValues { it.value.map { it.second } }

    fun postProcess(requestMapNode: RequestMapNode): RequestMapNode {
        val shapeName      = requestMapNode.shape?.c2jName ?: throw IllegalArgumentException("Can't process a node with a null shape")
        val postProcessors = shapeNameToPostProcessors[shapeName]

        return postProcessors?.fold(requestMapNode) { currentRequest, preprocessor ->
            preprocessor.process(currentRequest)
        } ?: requestMapNode
    }

}