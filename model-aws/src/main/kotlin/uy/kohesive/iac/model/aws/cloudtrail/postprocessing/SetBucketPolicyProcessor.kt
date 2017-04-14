package uy.kohesive.iac.model.aws.cloudtrail.postprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMapNode

class SetBucketPolicyProcessor : RequestNodePostProcessor {

    override val shapeNames = listOf("SetBucketPolicyRequest")

    override fun process(requestMapNode: RequestMapNode): RequestMapNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}