package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode

class CreateBucketProcessor : RequestNodePostProcessor {

    override val shapeNames = listOf("CreateBucketInput")

    override fun process(requestMapNode: RequestMapNode, awsModel: IntermediateModel): RequestMapNode = requestMapNode.members.firstOrNull {
        it.memberModel.c2jName == "BucketName"
    }?.let { bucketNameMember ->
        requestMapNode.copy(
            members         = (requestMapNode.members - bucketNameMember).toMutableList(),
            constructorArgs = (requestMapNode.constructorArgs + bucketNameMember).toMutableList()
        )
    } ?: throw IllegalStateException("CreateBucketRequest request does not contain bucket name")

}