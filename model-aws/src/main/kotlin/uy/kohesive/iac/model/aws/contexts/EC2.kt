package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.ec2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.utils.DslScope
import java.util.concurrent.atomic.AtomicInteger

@DslScope
class EC2Context(context: IacContext): BaseEC2Context(context) {

    private val runInstanceRequestsCounter = AtomicInteger()

    fun EC2Context.runInstances(preferredNamePrefix: String? = null, count: Int = 1, init: RunInstancesRequest.() -> Unit): Reservation {
        return ec2Client.runInstances(RunInstancesRequest().apply {
            preferredNamePrefix?.let { prefix ->
                registerWithName("$prefix${runInstanceRequestsCounter.getAndIncrement().takeIf { it > 0 }?.let { "_$it" } ?: ""}")
            }

            init(this)

            minCount = minCount ?: count
            maxCount = maxCount ?: count
        }).reservation
    }

    fun EC2Context.modifyPlacement(instanceId: String, init: ModifyInstancePlacementRequest.() -> Unit) {
        ec2Client.modifyInstancePlacement(ModifyInstancePlacementRequest().apply {
            withInstanceId(instanceId)
            init(this)
        })
    }

    /**
     * @return Security group ID
     */
    fun EC2Context.createSecurityGroup(groupName: String, init: CreateSecurityGroupRequest.() -> Unit): String {
        return ec2Client.createSecurityGroup(CreateSecurityGroupRequest().apply {
            withGroupName(groupName)
            init(this)
        }).groupId
    }

    fun EC2Context.authorizeSecurityGroupIngress(groupName: String, init: AuthorizeSecurityGroupIngressRequest.() -> Unit) {
        ec2Client.authorizeSecurityGroupIngress(AuthorizeSecurityGroupIngressRequest().apply {
            withGroupName(groupName)
            init(this)
        })
    }

}