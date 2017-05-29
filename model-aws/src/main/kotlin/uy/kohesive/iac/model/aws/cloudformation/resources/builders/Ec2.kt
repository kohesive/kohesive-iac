package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.ec2.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.EC2
import uy.kohesive.iac.model.aws.helpers.RunSingleEC2InstanceRequest

// TODO: RequestSpotInstancesRequest can also result RunInstancesRequest, also mind the 'AvailabilityZone' property

class Ec2InstancePropertiesBuilder : ResourcePropertiesBuilder<RunSingleEC2InstanceRequest> {

    override val requestClazz = RunSingleEC2InstanceRequest::class

    // TODO: SsmAssociations?
    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as RunSingleEC2InstanceRequest).originalRequest.let {
            val placementRequest = relatedObjects.filterIsInstance<ModifyInstancePlacementRequest>().lastOrNull()
            EC2.Instance(
                Affinity            = placementRequest?.affinity,
                BlockDeviceMappings = it.blockDeviceMappings?.map {
                    EC2.Instance.MappingProperty(
                        DeviceName  = it.deviceName,
                        NoDevice    = it.noDevice?.let { emptyMap<String, String>() },
                        VirtualName = it.virtualName,
                        Ebs         = it.ebs?.let {
                            EC2.Instance.MappingProperty.AmazonElasticBlockStoreProperty(
                                DeleteOnTermination = it.deleteOnTermination?.toString(),
                                VolumeType          = it.volumeType,
                                VolumeSize          = it.volumeSize?.toString(),
                                Iops                = it.iops?.toString(),
                                SnapshotId          = it.snapshotId,
                                Encrypted           = it.encrypted?.toString()
                            )
                        }
                    )
                },
                InstanceType       = it.instanceType,
                PrivateIpAddress   = it.privateIpAddress,
                SecurityGroupIds   = it.securityGroupIds,
                IamInstanceProfile = it.iamInstanceProfile?.let {
                    it.arn ?: it.name
                },
                ImageId                           = it.imageId,
                AdditionalInfo                    = it.additionalInfo,
                DisableApiTermination             = it.disableApiTermination?.toString(),
                EbsOptimized                      = it.ebsOptimized?.toString(),
                InstanceInitiatedShutdownBehavior = it.instanceInitiatedShutdownBehavior,
                Ipv6AddressCount                  = it.ipv6AddressCount?.toString(),
                Ipv6Addresses                     = it.ipv6Addresses?.map {
                    EC2.NetworkInterface.Ipv6AddressProperty(
                        Ipv6Address = it.ipv6Address
                    )
                },
                HostId            = placementRequest?.hostId,
                KernelId          = it.kernelId,
                KeyName           = it.keyName,
                Monitoring        = it.monitoring?.toString(),
                NetworkInterfaces = it.networkInterfaces?.map {
                    EC2.Instance.EmbeddedProperty(
                        AssociatePublicIpAddress = it.associatePublicIpAddress?.toString(),
                        Ipv6Addresses            = it.ipv6Addresses?.map {
                            EC2.NetworkInterface.Ipv6AddressProperty(
                                Ipv6Address = it.ipv6Address
                            )
                        },
                        Ipv6AddressCount    = it.ipv6AddressCount?.toString(),
                        PrivateIpAddress    = it.privateIpAddress,
                        DeleteOnTermination = it.deleteOnTermination?.toString(),
                        Description         = it.description,
                        DeviceIndex         = it.deviceIndex.toString(),
                        GroupSet            = it.groups,
                        NetworkInterfaceId  = it.networkInterfaceId,
                        PrivateIpAddresses  = it.privateIpAddresses?.map {
                            EC2.NetworkInterface.PrivateIPSpecificationProperty(
                                PrivateIpAddress = it.privateIpAddress,
                                Primary          = it.primary.toString()
                            )
                        },
                        SecondaryPrivateIpAddressCount = it.secondaryPrivateIpAddressCount?.toString(),
                        SubnetId                       = it.subnetId
                    )
                },
                SubnetId           = it.subnetId,
                PlacementGroupName = it.placement?.groupName,
                RamdiskId          = it.ramdiskId,
                Tenancy            = placementRequest?.tenancy,
                SecurityGroups     = it.securityGroups,
                SourceDestCheck    = relatedObjects.filterIsInstance<ModifyInstanceAttributeRequest>().lastOrNull()?.sourceDestCheck?.toString(),
                Volumes            = relatedObjects.filterIsInstance<AttachVolumeRequest>().map {
                    EC2.Instance.MountPointProperty(
                        Device   = it.device,
                        VolumeId = it.volumeId
                    )
                },
                UserData           = it.userData,
                Tags               = relatedObjects.getTags()
            )
        }
}

class Ec2HostPropertiesBuilder : ResourcePropertiesBuilder<AllocateHostsRequest> {

    override val requestClazz = AllocateHostsRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as AllocateHostsRequest).let {
            EC2.Host(
                AutoPlacement    = it.autoPlacement,
                InstanceType     = it.instanceType,
                AvailabilityZone = it.availabilityZone
            )
        }

}

class Ec2EIPPropertiesBuilder : ResourcePropertiesBuilder<AllocateAddressRequest> {

    override val requestClazz = AllocateAddressRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as AllocateAddressRequest).let {
            EC2.EIP(
                Domain     = it.domain,
                InstanceId = relatedObjects.filterIsInstance<AssociateAddressRequest>().firstOrNull()?.instanceId
            )
        }
}

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
                },
                SecurityGroupEgress = relatedObjects.filterIsInstance<AuthorizeSecurityGroupEgressRequest>().flatMap { request ->
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
                },
                VpcId = request.vpcId,
                Tags  = relatedObjects.getTags()
            )
        }
}

class EC2CustomerGatewayResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateCustomerGatewayRequest> {

    override val requestClazz = CreateCustomerGatewayRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateCustomerGatewayRequest).let {
            EC2.CustomerGateway(
                BgpAsn    = request.bgpAsn.toString(),
                IpAddress = request.publicIp,
                Type      = request.type,
                Tags      = relatedObjects.getTags()
            )
        }

}

class EC2DHCPOptionsResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDhcpOptionsRequest> {

    override val requestClazz = CreateDhcpOptionsRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDhcpOptionsRequest).let {
            fun getOptionValues(optionName: String): List<String>?
                = request.dhcpConfigurations.firstOrNull { it.key == optionName }?.values

            EC2.DHCPOptions(
                DomainName         = getOptionValues("domain-name")?.firstOrNull(),
                DomainNameServers  = getOptionValues("domain-name-servers"),
                NetbiosNameServers = getOptionValues("netbios-name-servers"),
                NetbiosNodeType    = getOptionValues("netbios-node-type"),
                NtpServers         = getOptionValues("ntp-servers"),
                Tags               = relatedObjects.getTags()
            )
        }

}

class EC2FlowLogResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateFlowLogsRequest> {

    override val requestClazz = CreateFlowLogsRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateFlowLogsRequest).let {
            EC2.FlowLog(
                ResourceId               = request.resourceIds.first(),
                DeliverLogsPermissionArn = request.deliverLogsPermissionArn,
                LogGroupName             = request.logGroupName,
                ResourceType             = request.resourceType,
                TrafficType              = request.trafficType
            )
        }

}

class EC2InternetGatewayResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateInternetGatewayRequest> {

    override val requestClazz = CreateInternetGatewayRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateInternetGatewayRequest).let {
            EC2.InternetGateway(
                Tags = relatedObjects.getTags()
            )
        }

}

class EC2NatGatewayResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateNatGatewayRequest> {

    override val requestClazz = CreateNatGatewayRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateNatGatewayRequest).let {
            EC2.NatGateway(
                AllocationId = request.allocationId,
                SubnetId     = request.subnetId
            )
        }

}

class EC2NetworkAclResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateNetworkAclRequest> {

    override val requestClazz = CreateNetworkAclRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateNetworkAclRequest).let {
            EC2.NetworkAcl(
                VpcId = request.vpcId,
                Tags  = relatedObjects.getTags()
            )
        }

}

class EC2NetworkAclEntryResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateNetworkAclEntryRequest> {

    override val requestClazz = CreateNetworkAclEntryRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateNetworkAclEntryRequest).let {
            EC2.NetworkAclEntry(
                CidrBlock     = request.cidrBlock,
                Egress        = request.egress?.toString(),
                Ipv6CidrBlock = request.ipv6CidrBlock,
                NetworkAclId  = request.networkAclId,
                PortRange     = request.portRange?.let {
                    EC2.NetworkAclEntry.PortRangeProperty(
                        From = it.from?.toString(),
                        To   = it.to?.toString()
                    )
                },
                Protocol      = request.protocol,
                RuleAction    = request.ruleAction,
                RuleNumber    = request.ruleNumber.toString(),
                Icmp          = request.icmpTypeCode?.let {
                    EC2.NetworkAclEntry.IcmpProperty(
                        Code = it.code?.toString(),
                        Type = it.type?.toString()
                    )
                }
            )
        }

}

class EC2NetworkInterfaceResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateNetworkInterfaceRequest> {

    override val requestClazz = CreateNetworkInterfaceRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateNetworkInterfaceRequest).let {
            EC2.NetworkInterface(
                Description      = request.description,
                GroupSet         = request.groups,
                Ipv6AddressCount = request.ipv6AddressCount?.toString(),
                Ipv6Addresses    = request.ipv6Addresses?.map {
                    EC2.NetworkInterface.Ipv6AddressProperty(
                        Ipv6Address = it.ipv6Address
                    )
                },
                PrivateIpAddress   = request.privateIpAddress,
                PrivateIpAddresses = request.privateIpAddresses?.map {
                    EC2.NetworkInterface.PrivateIPSpecificationProperty(
                        PrivateIpAddress = it.privateIpAddress,
                        Primary          = (it.primary ?: false).toString()
                    )
                },
                SecondaryPrivateIpAddressCount = request.secondaryPrivateIpAddressCount?.toString(),
                SubnetId                       = request.subnetId,
                Tags                           = relatedObjects.getTags(),
                SourceDestCheck                = relatedObjects.filterIsInstance<ModifyNetworkInterfaceAttributeRequest>().lastOrNull()?.let {
                    it.isSourceDestCheck?.toString()
                }
            )
        }

}

class EC2PlacementGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreatePlacementGroupRequest> {

    override val requestClazz = CreatePlacementGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreatePlacementGroupRequest).let {
            EC2.PlacementGroup(
                Strategy = request.strategy
            )
        }

}

class EC2RouteResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateRouteRequest> {

    override val requestClazz = CreateRouteRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateRouteRequest).let {
            EC2.Route(
                DestinationCidrBlock     = request.destinationCidrBlock,
                DestinationIpv6CidrBlock = request.destinationIpv6CidrBlock,
                GatewayId                = request.gatewayId,
                InstanceId               = request.instanceId,
                NatGatewayId             = request.natGatewayId,
                NetworkInterfaceId       = request.networkInterfaceId,
                RouteTableId             = request.routeTableId,
                VpcPeeringConnectionId   = request.vpcPeeringConnectionId
            )
        }

}

class EC2RouteTableResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateRouteTableRequest> {

    override val requestClazz = CreateRouteTableRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateRouteTableRequest).let {
            EC2.RouteTable(
                VpcId = request.vpcId,
                Tags  = relatedObjects.getTags()
            )
        }

}

class EC2SubnetResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateSubnetRequest> {

    override val requestClazz = CreateSubnetRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateSubnetRequest).let {
            EC2.Subnet(
                AvailabilityZone    = request.availabilityZone,
                CidrBlock           = request.cidrBlock,
                VpcId               = request.vpcId,
                MapPublicIpOnLaunch = relatedObjects.filterIsInstance<ModifySubnetAttributeRequest>().lastOrNull()?.mapPublicIpOnLaunch?.toString(),
                Tags                = relatedObjects.getTags()
            )
        }

}

class EC2VPCResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateVpcRequest> {

    override val requestClazz = CreateVpcRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateVpcRequest).let {
            val modifyRequest = relatedObjects.filterIsInstance<ModifyVpcAttributeRequest>().lastOrNull()

            EC2.VPC(
                CidrBlock          = request.cidrBlock,
                InstanceTenancy    = request.instanceTenancy,
                EnableDnsHostnames = modifyRequest?.enableDnsHostnames?.toString(),
                EnableDnsSupport   = modifyRequest?.enableDnsSupport?.toString(),
                Tags               = relatedObjects.getTags()
            )
        }

}

class EC2VPCEndpointResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateVpcEndpointRequest> {

    override val requestClazz = CreateVpcEndpointRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateVpcEndpointRequest).let {
            EC2.VPCEndpoint(
                PolicyDocument = request.policyDocument,
                RouteTableIds  = request.routeTableIds,
                ServiceName    = request.serviceName,
                VpcId          = request.vpcId
            )
        }

}

class EC2VPCPeeringConnectionResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateVpcPeeringConnectionRequest> {

    override val requestClazz = CreateVpcPeeringConnectionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateVpcPeeringConnectionRequest).let {
            EC2.VPCPeeringConnection(
                PeerVpcId = request.peerVpcId,
                VpcId     = request.vpcId,
                Tags      = relatedObjects.getTags()
            )
        }

}

class EC2VPNConnectionResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateVpnConnectionRequest> {

    override val requestClazz = CreateVpnConnectionRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateVpnConnectionRequest).let {
            EC2.VPNConnection(
                CustomerGatewayId = request.customerGatewayId,
                Type              = request.type,
                VpnGatewayId      = request.vpnGatewayId,
                StaticRoutesOnly  = request.options?.staticRoutesOnly?.toString(),
                Tags              = relatedObjects.getTags()
            )
        }

}

class EC2VPNConnectionRouteResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateVpnConnectionRouteRequest> {

    override val requestClazz = CreateVpnConnectionRouteRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateVpnConnectionRouteRequest).let {
            EC2.VPNConnectionRoute(
                DestinationCidrBlock = request.destinationCidrBlock,
                VpnConnectionId      = request.vpnConnectionId
            )
        }

}

class EC2VPNGatewayResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateVpnGatewayRequest> {

    override val requestClazz = CreateVpnGatewayRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateVpnGatewayRequest).let {
            EC2.VPNGateway(
                Type = request.type,
                Tags = relatedObjects.getTags()
            )
        }

}

class EC2VolumeResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateVolumeRequest> {

    override val requestClazz = CreateVolumeRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateVolumeRequest).let {
            EC2.Volume(
                AvailabilityZone = request.availabilityZone,
                AutoEnableIO     = relatedObjects.filterIsInstance<ModifyVolumeAttributeRequest>().lastOrNull()?.autoEnableIO?.toString(),
                Encrypted        = request.encrypted?.toString(),
                Iops             = request.iops?.toString(),
                KmsKeyId         = request.kmsKeyId,
                Size             = request.size?.toString(),
                SnapshotId       = request.snapshotId,
                VolumeType       = request.volumeType,
                Tags             = request.tagSpecifications?.flatMap { it.tags }?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
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

private fun AuthorizeSecurityGroupEgressRequest.ipPermissionFromBody()
    = if (ipPermissions.isEmpty()) {
        IpPermission()
            .withIpv4Ranges(this.cidrIp?.let { IpRange().withCidrIp(it) })
            .withToPort(this.toPort)
            .withFromPort(this.fromPort)
            .withIpProtocol(this.ipProtocol)
    } else {
        null
    }

private fun List<Any>.getTags() = filterIsInstance<CreateTagsRequest>().flatMap { it.tags }.map {
    CloudFormation.ResourceTag(
        Key   = it.key,
        Value = it.value
    )
}
