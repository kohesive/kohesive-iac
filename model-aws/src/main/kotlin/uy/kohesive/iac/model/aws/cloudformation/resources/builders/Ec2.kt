package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest
import com.amazonaws.services.ec2.model.IpPermission
import com.amazonaws.services.ec2.model.IpRange
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.EC2

class Ec2SecurityGroupPropertiesBuilder : ResourcePropertiesBuilder<CreateSecurityGroupRequest> {

    override val requestClazz = CreateSecurityGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateSecurityGroupRequest).let {
            EC2.SecurityGroup(
                GroupDescription     = it.description,
                SecurityGroupIngress = relatedObjects.filterIsInstance<AuthorizeSecurityGroupIngressRequest>().flatMap { request ->
                    (listOf(request.ipPermissionFromBody()) + request.ipPermissions).filterNotNull()
                }.flatMap { rule ->
                    (rule.ipv6Ranges.map { it.cidrIpv6 } + rule.ipv4Ranges.map { it.cidrIp }).map {
                        rule to it
                    }
                }.map { ruleToCidrPair ->
                    EC2.SecurityGroup.RuleProperty(
                        CidrIp     = ruleToCidrPair.second,
                        IpProtocol = ruleToCidrPair.first.ipProtocol,
                        FromPort   = ruleToCidrPair.first.fromPort?.toString(),
                        ToPort     = ruleToCidrPair.first.toPort?.toString()
                    )
                }
            )
        }
}

private fun AuthorizeSecurityGroupIngressRequest.ipPermissionFromBody()
    = if (ipPermissions.isEmpty()) {
        IpPermission()
            .withIpv4Ranges(this.cidrIp?.let { IpRange().withCidrIp(it) })
            .withToPort(this.toPort)
            .withFromPort(this.fromPort)
            .withIpProtocol(this.ipProtocol)
    } else {
        null
    }