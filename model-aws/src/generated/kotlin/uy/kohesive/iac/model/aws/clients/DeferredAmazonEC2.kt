package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.ec2.AbstractAmazonEC2
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonEC2(val context: IacContext) : AbstractAmazonEC2(), AmazonEC2 {

    override fun attachClassicLinkVpc(request: AttachClassicLinkVpcRequest): AttachClassicLinkVpcResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AttachClassicLinkVpcRequest, AttachClassicLinkVpcResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun attachInternetGateway(request: AttachInternetGatewayRequest): AttachInternetGatewayResult {
        return with (context) {
            request.registerWithAutoName()
            AttachInternetGatewayResult().registerWithSameNameAs(request)
        }
    }

    override fun attachNetworkInterface(request: AttachNetworkInterfaceRequest): AttachNetworkInterfaceResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AttachNetworkInterfaceRequest, AttachNetworkInterfaceResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun attachVolume(request: AttachVolumeRequest): AttachVolumeResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AttachVolumeRequest, AttachVolumeResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun attachVpnGateway(request: AttachVpnGatewayRequest): AttachVpnGatewayResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AttachVpnGatewayRequest, AttachVpnGatewayResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createCustomerGateway(request: CreateCustomerGatewayRequest): CreateCustomerGatewayResult {
        return with (context) {
            request.registerWithAutoName()
            CreateCustomerGatewayResult().withCustomerGateway(
                makeProxy<CreateCustomerGatewayRequest, CustomerGateway>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateCustomerGatewayRequest::getType to CustomerGateway::getType
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createDhcpOptions(request: CreateDhcpOptionsRequest): CreateDhcpOptionsResult {
        return with (context) {
            request.registerWithAutoName()
            CreateDhcpOptionsResult().withDhcpOptions(
                makeProxy<CreateDhcpOptionsRequest, DhcpOptions>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateDhcpOptionsRequest::getDhcpConfigurations to DhcpOptions::getDhcpConfigurations
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createEgressOnlyInternetGateway(request: CreateEgressOnlyInternetGatewayRequest): CreateEgressOnlyInternetGatewayResult {
        return with (context) {
            request.registerWithAutoName()
            CreateEgressOnlyInternetGatewayResult().withEgressOnlyInternetGateway(
                makeProxy<CreateEgressOnlyInternetGatewayRequest, EgressOnlyInternetGateway>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createFlowLogs(request: CreateFlowLogsRequest): CreateFlowLogsResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateFlowLogsRequest, CreateFlowLogsResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateFlowLogsRequest::getClientToken to CreateFlowLogsResult::getClientToken
                )
            )
        }
    }

    override fun createImage(request: CreateImageRequest): CreateImageResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateImageRequest, CreateImageResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createInstanceExportTask(request: CreateInstanceExportTaskRequest): CreateInstanceExportTaskResult {
        return with (context) {
            request.registerWithAutoName()
            CreateInstanceExportTaskResult().withExportTask(
                makeProxy<CreateInstanceExportTaskRequest, ExportTask>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateInstanceExportTaskRequest::getDescription to ExportTask::getDescription
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createInternetGateway(request: CreateInternetGatewayRequest): CreateInternetGatewayResult {
        return with (context) {
            request.registerWithAutoName()
            CreateInternetGatewayResult().withInternetGateway(
                makeProxy<CreateInternetGatewayRequest, InternetGateway>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createKeyPair(request: CreateKeyPairRequest): CreateKeyPairResult {
        return with (context) {
            request.registerWithAutoName()
            CreateKeyPairResult().withKeyPair(
                makeProxy<CreateKeyPairRequest, KeyPair>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateKeyPairRequest::getKeyName to KeyPair::getKeyName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createNatGateway(request: CreateNatGatewayRequest): CreateNatGatewayResult {
        return with (context) {
            request.registerWithAutoName()
            CreateNatGatewayResult().withNatGateway(
                makeProxy<CreateNatGatewayRequest, NatGateway>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateNatGatewayRequest::getSubnetId to NatGateway::getSubnetId
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createNetworkAcl(request: CreateNetworkAclRequest): CreateNetworkAclResult {
        return with (context) {
            request.registerWithAutoName()
            CreateNetworkAclResult().withNetworkAcl(
                makeProxy<CreateNetworkAclRequest, NetworkAcl>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateNetworkAclRequest::getVpcId to NetworkAcl::getVpcId
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createNetworkAclEntry(request: CreateNetworkAclEntryRequest): CreateNetworkAclEntryResult {
        return with (context) {
            request.registerWithAutoName()
            CreateNetworkAclEntryResult().registerWithSameNameAs(request)
        }
    }

    override fun createNetworkInterface(request: CreateNetworkInterfaceRequest): CreateNetworkInterfaceResult {
        return with (context) {
            request.registerWithAutoName()
            CreateNetworkInterfaceResult().withNetworkInterface(
                makeProxy<CreateNetworkInterfaceRequest, NetworkInterface>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateNetworkInterfaceRequest::getSubnetId to NetworkInterface::getSubnetId,
                        CreateNetworkInterfaceRequest::getDescription to NetworkInterface::getDescription,
                        CreateNetworkInterfaceRequest::getPrivateIpAddress to NetworkInterface::getPrivateIpAddress
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createPlacementGroup(request: CreatePlacementGroupRequest): CreatePlacementGroupResult {
        return with (context) {
            request.registerWithAutoName()
            CreatePlacementGroupResult().registerWithSameNameAs(request)
        }
    }

    override fun createReservedInstancesListing(request: CreateReservedInstancesListingRequest): CreateReservedInstancesListingResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateReservedInstancesListingRequest, CreateReservedInstancesListingResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createRoute(request: CreateRouteRequest): CreateRouteResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateRouteRequest, CreateRouteResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createRouteTable(request: CreateRouteTableRequest): CreateRouteTableResult {
        return with (context) {
            request.registerWithAutoName()
            CreateRouteTableResult().withRouteTable(
                makeProxy<CreateRouteTableRequest, RouteTable>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateRouteTableRequest::getVpcId to RouteTable::getVpcId
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createSecurityGroup(request: CreateSecurityGroupRequest): CreateSecurityGroupResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateSecurityGroupRequest, CreateSecurityGroupResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createSnapshot(request: CreateSnapshotRequest): CreateSnapshotResult {
        return with (context) {
            request.registerWithAutoName()
            CreateSnapshotResult().withSnapshot(
                makeProxy<CreateSnapshotRequest, Snapshot>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateSnapshotRequest::getVolumeId to Snapshot::getVolumeId,
                        CreateSnapshotRequest::getDescription to Snapshot::getDescription
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createSpotDatafeedSubscription(request: CreateSpotDatafeedSubscriptionRequest): CreateSpotDatafeedSubscriptionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateSpotDatafeedSubscriptionResult().withSpotDatafeedSubscription(
                makeProxy<CreateSpotDatafeedSubscriptionRequest, SpotDatafeedSubscription>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateSpotDatafeedSubscriptionRequest::getBucket to SpotDatafeedSubscription::getBucket,
                        CreateSpotDatafeedSubscriptionRequest::getPrefix to SpotDatafeedSubscription::getPrefix
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createSubnet(request: CreateSubnetRequest): CreateSubnetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateSubnetResult().withSubnet(
                makeProxy<CreateSubnetRequest, Subnet>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateSubnetRequest::getVpcId to Subnet::getVpcId,
                        CreateSubnetRequest::getCidrBlock to Subnet::getCidrBlock,
                        CreateSubnetRequest::getAvailabilityZone to Subnet::getAvailabilityZone
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createTags(request: CreateTagsRequest): CreateTagsResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTagsResult().registerWithSameNameAs(request)
        }
    }

    override fun createVolume(request: CreateVolumeRequest): CreateVolumeResult {
        return with (context) {
            request.registerWithAutoName()
            CreateVolumeResult().withVolume(
                makeProxy<CreateVolumeRequest, Volume>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateVolumeRequest::getSize to Volume::getSize,
                        CreateVolumeRequest::getSnapshotId to Volume::getSnapshotId,
                        CreateVolumeRequest::getAvailabilityZone to Volume::getAvailabilityZone,
                        CreateVolumeRequest::getVolumeType to Volume::getVolumeType,
                        CreateVolumeRequest::getIops to Volume::getIops,
                        CreateVolumeRequest::getEncrypted to Volume::getEncrypted,
                        CreateVolumeRequest::getKmsKeyId to Volume::getKmsKeyId
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createVpc(request: CreateVpcRequest): CreateVpcResult {
        return with (context) {
            request.registerWithAutoName()
            CreateVpcResult().withVpc(
                makeProxy<CreateVpcRequest, Vpc>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateVpcRequest::getCidrBlock to Vpc::getCidrBlock,
                        CreateVpcRequest::getInstanceTenancy to Vpc::getInstanceTenancy
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createVpcEndpoint(request: CreateVpcEndpointRequest): CreateVpcEndpointResult {
        return with (context) {
            request.registerWithAutoName()
            CreateVpcEndpointResult().withVpcEndpoint(
                makeProxy<CreateVpcEndpointRequest, VpcEndpoint>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateVpcEndpointRequest::getVpcId to VpcEndpoint::getVpcId,
                        CreateVpcEndpointRequest::getServiceName to VpcEndpoint::getServiceName,
                        CreateVpcEndpointRequest::getPolicyDocument to VpcEndpoint::getPolicyDocument,
                        CreateVpcEndpointRequest::getRouteTableIds to VpcEndpoint::getRouteTableIds
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createVpcPeeringConnection(request: CreateVpcPeeringConnectionRequest): CreateVpcPeeringConnectionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateVpcPeeringConnectionResult().withVpcPeeringConnection(
                makeProxy<CreateVpcPeeringConnectionRequest, VpcPeeringConnection>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createVpnConnection(request: CreateVpnConnectionRequest): CreateVpnConnectionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateVpnConnectionResult().withVpnConnection(
                makeProxy<CreateVpnConnectionRequest, VpnConnection>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateVpnConnectionRequest::getType to VpnConnection::getType,
                        CreateVpnConnectionRequest::getCustomerGatewayId to VpnConnection::getCustomerGatewayId,
                        CreateVpnConnectionRequest::getVpnGatewayId to VpnConnection::getVpnGatewayId
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createVpnConnectionRoute(request: CreateVpnConnectionRouteRequest): CreateVpnConnectionRouteResult {
        return with (context) {
            request.registerWithAutoName()
            CreateVpnConnectionRouteResult().registerWithSameNameAs(request)
        }
    }

    override fun createVpnGateway(request: CreateVpnGatewayRequest): CreateVpnGatewayResult {
        return with (context) {
            request.registerWithAutoName()
            CreateVpnGatewayResult().withVpnGateway(
                makeProxy<CreateVpnGatewayRequest, VpnGateway>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateVpnGatewayRequest::getType to VpnGateway::getType,
                        CreateVpnGatewayRequest::getAvailabilityZone to VpnGateway::getAvailabilityZone
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

