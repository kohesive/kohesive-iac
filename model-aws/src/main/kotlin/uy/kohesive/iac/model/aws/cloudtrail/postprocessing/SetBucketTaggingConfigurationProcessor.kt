package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.model.intermediate.ListModel
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNodeMember

class SetBucketTaggingConfigurationProcessor : RequestNodePostProcessor {

    override val shapeNames = listOf("SetBucketTaggingConfigurationInput")

    override fun process(requestMapNode: RequestMapNode, awsModel: IntermediateModel): RequestMapNode {
        requestMapNode.constructorArgs += requestMapNode.members
        requestMapNode.members.clear()

        requestMapNode.constructorArgs.firstOrNull { it.memberModel.name == "TaggingConfiguration" }?.let { tagConfig ->
            tagConfig.value?.members?.firstOrNull { it.memberModel.name.startsWith("TagSet") }?.let { tagSetMember ->
                tagSetMember.memberModel.name = "TagSets"
                tagSetMember.memberModel.c2jName = "TagSets"
                tagSetMember.memberModel.setterMethodName = "withTagSets"

                tagSetMember.value?.members?.forEach { tagMember ->
                    (tagMember.value?.simpleValue as? Map<String, String>)?.let { tagMap ->
                        tagMap["Key"] to tagMap["Value"]
                    }?.let { keyValuePair ->
                        tagMember.value?.simpleType  = null
                        tagMember.value?.simpleValue = null
                        tagMember.value?.listModel   = ListModel(null, null, null, null, null)
                        tagMember.value?.vararg      = true
                        tagMember.value?.members?.addAll(listOf(
                            RequestMapNodeMember(tagMember.memberModel, RequestMapNode.simple("String", keyValuePair.first)),
                            RequestMapNodeMember(tagMember.memberModel, RequestMapNode.simple("String", keyValuePair.second))
                        ))
                    }
                }
            }
        }

        return requestMapNode
    }
}