package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.ec2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.KohesiveReference
import uy.kohesive.iac.model.aws.proxy.ReferenceParseException
import uy.kohesive.iac.model.aws.proxy.makeProxy

class DeferredAmazonEC2(context: IacContext) : BaseDeferredAmazonEC2(context) {

    override fun createSecurityGroup(request: CreateSecurityGroupRequest): CreateSecurityGroupResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy(context, getNameStrict(request), request)
        }
    }

    override fun authorizeSecurityGroupIngress(request: AuthorizeSecurityGroupIngressRequest): AuthorizeSecurityGroupIngressResult {
        return with (context) {
            // First, we need to make sure we have a name here
            val nameToRegisterWith: String = request.groupName ?: request.groupId?.let { groupId ->
                try {
                    KohesiveReference.fromString(groupId).targetId
                } catch (t: ReferenceParseException) {
                    // Not a reference
                    null
                }
            } ?:  throw IllegalArgumentException("Can't bind request to a known security group request")

            request.registerWithName(nameToRegisterWith)
            AuthorizeSecurityGroupIngressResult().registerWithSameNameAs(request)
        }
    }

    override fun runInstances(request: RunInstancesRequest): RunInstancesResult {
        return with (context) {
            val requestName = getNameStrict(request)
            if (request.minCount == null || request.minCount < 1 || request.minCount != request.maxCount) {
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