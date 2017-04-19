package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode

class SetBucketLoggingConfigurationProcessor : RequestNodePostProcessor {

    override val shapeNames = listOf("SetBucketLoggingConfigurationInput")

    override fun process(requestMapNode: RequestMapNode, awsModel: IntermediateModel): RequestMapNode {
        requestMapNode.constructorArgs += requestMapNode.members
        requestMapNode.members.clear()

        return requestMapNode
    }
}