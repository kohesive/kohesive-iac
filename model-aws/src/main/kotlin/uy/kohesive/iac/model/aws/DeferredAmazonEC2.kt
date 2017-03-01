package uy.kohesive.iac.model.aws

import com.amazonaws.services.ec2.AbstractAmazonEC2
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Reservation
import com.amazonaws.services.ec2.model.RunInstancesRequest
import com.amazonaws.services.ec2.model.RunInstancesResult
import uy.kohesive.iac.model.aws.proxy.makeProxy

class DeferredAmazonEC2(val context: IacContext) : AbstractAmazonEC2(), AmazonEC2 {
    override fun runInstances(request: RunInstancesRequest): RunInstancesResult {
        return with (context) {
            val requestName = getNameStrict(request)
            if (request.minCount == null || request.minCount < 1 ||  request.minCount != request.maxCount) {
                throw IllegalArgumentException("minCount & maxCount must be not-null, equal and positive")
            }

//            val securityGroups = makeListProxy()

            RunInstancesResult().registerWithName(requestName).apply {
                withReservation(makeProxy<RunInstancesRequest, Reservation>(context, "$requestName-Reservation", request)
                    // Instances
                    .withInstances((0..request.minCount - 1).map { instanceIdx ->
                        makeProxy<RunInstancesRequest, Instance>(context, "$requestName[$instanceIdx]", request, mapOf(
                            RunInstancesRequest::getImageId             to Instance::getImageId,
                            RunInstancesRequest::getClientToken         to Instance::getClientToken,
                            RunInstancesRequest::getEbsOptimized        to Instance::getEbsOptimized,
                            RunInstancesRequest::getInstanceType        to Instance::getInstanceType,
                            RunInstancesRequest::getKernelId            to Instance::getKernelId,
                            RunInstancesRequest::getKeyName             to Instance::getKeyName,
                            RunInstancesRequest::getNetworkInterfaces   to Instance::getNetworkInterfaces,
                            RunInstancesRequest::getPlacement           to Instance::getPlacement,
                            RunInstancesRequest::getRamdiskId           to Instance::getRamdiskId,
                            // TODO: this is wrong. How do we construct these?
                            RunInstancesRequest::getSecurityGroups      to Instance::getSecurityGroups,
                            RunInstancesRequest::getSubnetId            to Instance::getSubnetId
                        ))
                    })

                    // TODO: rest of 'Instance' properties
                )
            }
        }
    }

}