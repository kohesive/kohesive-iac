package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNodeMember

class SetBucketAclProcessor : RequestNodePostProcessor {

    companion object {
        val GroupGranteeEnumFix = mapOf(
            "HttpAcsAmazonawsComgroupss3LogDelivery" to "LogDelivery"
        )
    }

    override val shapeNames = listOf("SetBucketAclInput")

    override fun process(requestMapNode: RequestMapNode, awsModel: IntermediateModel): RequestMapNode {
        val grantsNode = requestMapNode.members.firstOrNull { it.memberModel.name == "Acl" }?.value?.members?.firstOrNull {
            it.memberModel.name == "Grants"
        }?.value

        val grantsMembers = grantsNode?.members?.map(RequestMapNodeMember::value)?.filterNotNull().orEmpty()

        val grantees = grantsMembers.filterNotNull().map {
            it.members.firstOrNull {
                it.memberModel.name == "Grantee"
            }?.value
        }.filterNotNull().orEmpty()

        grantees.forEach { grantee ->
            if (grantee.shape?.shapeName == "GroupGrantee") {
                grantee.enumValue = grantee.enumValue?.let { enumValue ->
                    GroupGranteeEnumFix[enumValue]
                } ?: grantee.enumValue
            }
        }

        grantsMembers.forEach { grant ->
            grant.constructorArgs += grant.members
            grant.members.clear()
        }

        requestMapNode.constructorArgs += requestMapNode.members
        requestMapNode.members.clear()

        return requestMapNode
    }

}