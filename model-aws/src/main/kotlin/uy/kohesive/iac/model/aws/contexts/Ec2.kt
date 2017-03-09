package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface Ec2Identifiable : KohesiveIdentifiable {}

interface Ec2Enabled : Ec2Identifiable {
    val ec2Client: AmazonEC2
    val ec2Context: Ec2Context
    fun <T> withEc2Context(init: Ec2Context.(AmazonEC2)->T): T = ec2Context.init(ec2Client)
}

@DslScope
class Ec2Context(private val context: IacContext): Ec2Enabled by context {

    /**
     * @return Security group ID
     */
    fun Ec2Context.createSecurityGroup(groupName: String, init: CreateSecurityGroupRequest.() -> Unit): String {
        return ec2Client.createSecurityGroup(CreateSecurityGroupRequest().apply {
            withGroupName(groupName)
            init(this)
        }).groupId
    }

    fun Ec2Context.authorizeSecurityGroupIngress(groupName: String, init: AuthorizeSecurityGroupIngressRequest.() -> Unit) {
        ec2Client.authorizeSecurityGroupIngress(AuthorizeSecurityGroupIngressRequest().apply {
            withGroupName(groupName)
            init(this)
        })
    }

}