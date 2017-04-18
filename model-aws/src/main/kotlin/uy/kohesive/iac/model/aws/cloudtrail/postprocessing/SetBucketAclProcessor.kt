package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNodeMember

class SetBucketAclProcessor : RequestNodePostProcessor {

    override val shapeNames = listOf("SetBucketAclInput")

    override fun process(requestMapNode: RequestMapNode, awsModel: IntermediateModel): RequestMapNode {
        val grantees = requestMapNode.members.firstOrNull { it.memberModel.name == "Acl" }?.value?.members?.firstOrNull {
            it.memberModel.name == "Grants"
        }?.value?.members?.map(RequestMapNodeMember::value)?.filterNotNull()?.map {
            it.members.firstOrNull {
                it.memberModel.name == "Grantee"
            }?.value
        }?.filterNotNull().orEmpty()

        grantees.forEach { grantee ->
            val granteeType = grantee.members.firstOrNull { member ->
                member.memberModel.name == "xsi:type"
            }?.value?.simpleValue ?: throw IllegalArgumentException("Can't figure out grantee type")

            println()
        }

        return requestMapNode
    }

}