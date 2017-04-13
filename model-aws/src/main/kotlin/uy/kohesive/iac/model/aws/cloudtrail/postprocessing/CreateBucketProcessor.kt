package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode

class CreateBucketProcessor : RequestNodePostProcessor {

    override val shapeNames = listOf("CreateBucketInput")

    override fun process(requestMapNode: RequestMapNode): RequestMapNode = requestMapNode.members.firstOrNull {
        it.memberModel.c2jName == "BucketName"
    }?.let { bucketNameMember ->
        requestMapNode.copy(
            members         = requestMapNode.members - bucketNameMember,
            constructorArgs = requestMapNode.constructorArgs + bucketNameMember
        )
    } ?: throw IllegalStateException("CreateBucketRequest request does not contain bucket name")

}