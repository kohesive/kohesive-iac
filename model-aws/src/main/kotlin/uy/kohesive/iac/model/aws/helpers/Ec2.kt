package uy.kohesive.iac.model.aws.helpers

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.ec2.model.RunInstancesRequest

data class RunSingleEC2InstanceRequest(
    val originalRequest: RunInstancesRequest,
    val requestNamePrefix: String,
    val index: Int = 0
) : AmazonWebServiceRequest() {

    fun getKohesiveName() = if (index == 0) requestNamePrefix else "${requestNamePrefix}_$index"

}