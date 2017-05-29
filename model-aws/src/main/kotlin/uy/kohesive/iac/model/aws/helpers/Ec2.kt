package uy.kohesive.iac.model.aws.helpers

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.ec2.model.ModifyInstancePlacementRequest
import com.amazonaws.services.ec2.model.RunInstancesRequest
import uy.kohesive.iac.model.aws.proxy.KohesiveReference
import uy.kohesive.iac.model.aws.proxy.isKohesiveRef

data class RunSingleEC2InstanceRequest(
    val originalRequest: RunInstancesRequest,
    val requestNamePrefix: String,
    val index: Int = 0
) : AmazonWebServiceRequest() {

    fun getKohesiveName() = if (index == 0) requestNamePrefix else "${requestNamePrefix}_$index"

}

fun ModifyInstancePlacementRequest.getInstanceNameFromId() = if (instanceId.isKohesiveRef()) {
    KohesiveReference.fromString(instanceId).targetId
} else {
    instanceId
}
