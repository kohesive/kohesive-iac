package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.model.intermediate.MemberModel
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
        // Change 'setGrants' to 'grantAllPermissions'
        val grantsMember = requestMapNode.members.firstOrNull { it.memberModel.name == "Acl" }?.value?.members?.firstOrNull {
            it.memberModel.name == "Grants"
        }
        if (grantsMember != null) {
            val customSetterModel = MemberModel().apply {
                setterMethodName = "grantAllPermissions"
            }
            grantsMember.memberModel = customSetterModel
        }

        // Make grants 'vararg' to avoid listOf()
        val grantsNode = grantsMember?.value
        grantsNode?.vararg = true

        val grantsMembers = grantsNode?.members?.map(RequestMapNodeMember::value)?.filterNotNull().orEmpty()
        val grantees      = grantsMembers.filterNotNull().map {
            it.members.firstOrNull {
                it.memberModel.name == "Grantee"
            }?.value
        }.filterNotNull().orEmpty()

        // Fix GroupGrantee enum names
        // Fix CanonicalGrantee identity member location -> constructor arg
        grantees.forEach { grantee ->
            if (grantee.shape?.shapeName == "GroupGrantee") {
                grantee.enumValue = grantee.enumValue?.let { enumValue ->
                    GroupGranteeEnumFix[enumValue]
                } ?: grantee.enumValue
            } else if (grantee.shape?.shapeName == "CanonicalGrantee") {
                grantee.members.firstOrNull {
                    it.memberModel.name == "Identifier"
                }?.let { idMember ->
                    grantee.members.remove(idMember)
                    grantee.constructorArgs.add(idMember)
                }
            }
        }

        // Move grant's members to constructor args
        grantsMembers.forEach { grant ->
            grant.constructorArgs += grant.members
            grant.members.clear()
        }

        // Move request members to constructor args
        requestMapNode.constructorArgs += requestMapNode.members.take(2)
        repeat(2) {
            requestMapNode.members.removeAt(0)
        }

        return requestMapNode
    }

}