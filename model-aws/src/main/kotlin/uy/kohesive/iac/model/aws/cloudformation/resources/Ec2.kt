package uy.kohesive.iac.model.aws.cloudformation.resources

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest
import com.amazonaws.services.ec2.model.IpPermission
import com.amazonaws.services.ec2.model.IpRange
import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder

class Ec2SecurityGroupPropertiesBuilder : ResourcePropertiesBuilder<CreateSecurityGroupRequest> {

    override val requestClazz = CreateSecurityGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateSecurityGroupRequest).let {
            Ec2SecurityGroupResourceProperties(
                GroupDescription     = it.description,
                SecurityGroupIngress = relatedObjects.filterIsInstance<AuthorizeSecurityGroupIngressRequest>().flatMap { request ->
                    (listOf(request.ipPermissionFromBody()) + request.ipPermissions).filterNotNull()
                }.flatMap { rule ->
                    (rule.ipv6Ranges.map { it.cidrIpv6 } + rule.ipv4Ranges.map { it.cidrIp }).map {
                        rule to it
                    }
                }.map { ruleToCidrPair ->
                    SecurityGroupIngress(
                        CidrIp     = ruleToCidrPair.second,
                        IpProtocol = ruleToCidrPair.first.ipProtocol,
                        FromPort   = ruleToCidrPair.first.fromPort?.toString(),
                        ToPort     = ruleToCidrPair.first.toPort?.toString()
                    )
                }
            )
        }
}

private fun AuthorizeSecurityGroupIngressRequest.ipPermissionFromBody(): IpPermission? {
    return if (ipPermissions.isEmpty()) {
        IpPermission()
            .withIpv4Ranges(this.cidrIp?.let { IpRange().withCidrIp(it) })
            .withToPort(this.toPort)
            .withFromPort(this.fromPort)
            .withIpProtocol(this.ipProtocol)
    } else {
        null
    }
}

data class Ec2SecurityGroupResourceProperties(
    val GroupDescription: String?,
    val SecurityGroupIngress: List<SecurityGroupIngress>?
) : ResourceProperties

data class SecurityGroupIngress(
    val IpProtocol: String?,
    val FromPort: String?,
    val ToPort: String?,
    val CidrIp: String?
)