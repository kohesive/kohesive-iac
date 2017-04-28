package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode

class SetBucketWebsiteConfigurationProcessor : RequestNodePostProcessor {

    override val shapeNames = listOf("SetBucketWebsiteConfigurationInput")

    override fun process(requestMapNode: RequestMapNode, awsModel: IntermediateModel): RequestMapNode {
        requestMapNode.constructorArgs += requestMapNode.members
        requestMapNode.members.clear()

        return requestMapNode
    }
}