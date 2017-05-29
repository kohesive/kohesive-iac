package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest
import com.amazonaws.services.ec2.model.Reservation
import com.amazonaws.services.ec2.model.RunInstancesRequest
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