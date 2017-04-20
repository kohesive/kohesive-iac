package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode

class SetBucketTaggingConfigurationProcessor : RequestNodePostProcessor {

    override val shapeNames = listOf("SetBucketTaggingConfigurationInput")

    override fun process(requestMapNode: RequestMapNode, awsModel: IntermediateModel): RequestMapNode {
        requestMapNode.constructorArgs += requestMapNode.members
        requestMapNode.members.clear()

        requestMapNode.constructorArgs.firstOrNull { it.memberModel.name == "TaggingConfiguration" }?.let { tagConfig ->
            tagConfig.value?.members?.firstOrNull { it.memberModel.name == "TagSet" }?.let { tagSetModel ->
                tagSetModel.memberModel.name = "TagSets"
                tagSetModel.memberModel.c2jName = "TagSets"
                tagSetModel.memberModel.setterMethodName = "setTagSets"
            }
        }

        return requestMapNode
    }
}