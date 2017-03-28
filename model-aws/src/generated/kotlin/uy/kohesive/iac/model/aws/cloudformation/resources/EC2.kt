package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object EC2 {

    @CloudFormationType("AWS::EC2::CustomerGateway")
    data class CustomerGateway(
        val BgpAsn: String,
        val IpAddress: String,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val Type: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::DHCPOptions")
    data class DHCPOptions(
        val DomainName: String? = null,
        val DomainNameServers: List<String>? = null,
        val NetbiosNameServers: List<String>? = null,
        val NetbiosNodeType: List<String>? = null,
        val NtpServers: List<String>? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::EIP")
    data class EIP(
        val InstanceId: String? = null,
        val Domain: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::EIPAssociation")
    data class EIPAssociation(
        val AllocationId: String? = null,
        val EIP: String? = null,
        val InstanceId: String? = null,
        val NetworkInterfaceId: String? = null,
        val PrivateIpAddress: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::FlowLog")
    data class FlowLog(
        val DeliverLogsPermissionArn: String,
        val LogGroupName: String,
        val ResourceId: String,
        val ResourceType: String,
        val TrafficType: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::Host")
    data class Host(
        val AutoPlacement: String? = null,
        val AvailabilityZone: String,
        val InstanceType: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::Instance")
    data class Instance(
        val Affinity: String? = null,
        val AvailabilityZone: String? = null,
        val BlockDeviceMappings: List<EC2.Instance.MappingProperty>? = null,
        val DisableApiTermination: String? = null,
        val EbsOptimized: String? = null,
        val HostId: String? = null,
        val IamInstanceProfile: String? = null,
        val ImageId: String,
        val InstanceInitiatedShutdownBehavior: String? = null,
        val InstanceType: String? = null,
        val Ipv6AddressCount: String? = null,
        val Ipv6Addresses: List<EC2.NetworkInterface.Ipv6AddressProperty>? = null,
        val KernelId: String? = null,
        val KeyName: String? = null,
        val Monitoring: String? = null,
        val NetworkInterfaces: List<EC2.Instance.EmbeddedProperty>? = null,
        val PlacementGroupName: String? = null,
        val PrivateIpAddress: String? = null,
        val RamdiskId: String? = null,
        val SecurityGroupIds: List<String>? = null,
        val SecurityGroups: List<String>? = null,
        val SourceDestCheck: String? = null,
        val SsmAssociations: List<EC2.Instance.SsmAssociationProperty>? = null,
        val SubnetId: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val Tenancy: String? = null,
        val UserData: String? = null,
        val Volumes: List<EC2.Instance.MountPointProperty>? = null,
        val AdditionalInfo: String? = null
    ) : ResourceProperties {

        data class MappingProperty(
            val DeviceName: String,
            val Ebs: Instance.MappingProperty.AmazonElasticBlockStoreBlockDeviceTypeIsAnEmbeddedProperty? = null,
            val NoDevice: Map<String, String>? = null,
            val VirtualName: String? = null
        ) {

            data class AmazonElasticBlockStoreBlockDeviceTypeIsAnEmbeddedProperty(
                val DeleteOnTermination: String? = null,
                val Encrypted: String? = null,
                val Iops: String? = null,
                val SnapshotId: String? = null,
                val VolumeSize: String? = null,
                val VolumeType: String? = null
            ) 

        }


        data class EmbeddedProperty(
            val AssociatePublicIpAddress: String? = null,
            val DeleteOnTermination: String? = null,
            val Description: String? = null,
            val DeviceIndex: String,
            val GroupSet: List<String>? = null,
            val NetworkInterfaceId: String? = null,
            val Ipv6AddressCount: String? = null,
            val Ipv6Addresses: List<EC2.NetworkInterface.Ipv6AddressProperty>? = null,
            val PrivateIpAddress: String? = null,
            val PrivateIpAddresses: List<EC2.NetworkInterface.PrivateIPSpecificationProperty>? = null,
            val SecondaryPrivateIpAddressCount: String? = null,
            val SubnetId: String? = null
        ) 


        data class SsmAssociationProperty(
            val AssociationParameters: List<EC2.Instance.SsmAssociationProperty.AssociationParameterProperty>? = null,
            val DocumentName: String
        ) {

            data class AssociationParameterProperty(
                val Key: String,
                val Value: List<String>
            ) 

        }


        data class MountPointProperty(
            val Device: String,
            val VolumeId: String
        ) 

    }

    @CloudFormationType("AWS::EC2::InternetGateway")
    data class InternetGateway(
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::NatGateway")
    data class NatGateway(
        val AllocationId: String,
        val SubnetId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::NetworkAcl")
    data class NetworkAcl(
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val VpcId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::NetworkAclEntry")
    data class NetworkAclEntry(
        val CidrBlock: String? = null,
        val Egress: String? = null,
        val Icmp: NetworkAclEntry.IcmpProperty? = null,
        val Ipv6CidrBlock: String? = null,
        val NetworkAclId: String,
        val PortRange: NetworkAclEntry.PortRangeProperty? = null,
        val Protocol: String,
        val RuleAction: String,
        val RuleNumber: String
    ) : ResourceProperties {

        data class IcmpProperty(
            val Code: String? = null,
            val Type: String? = null
        ) 


        data class PortRangeProperty(
            val From: String? = null,
            val To: String? = null
        ) 

    }

    @CloudFormationType("AWS::EC2::NetworkInterface")
    data class NetworkInterface(
        val Description: String? = null,
        val GroupSet: List<String>? = null,
        val Ipv6AddressCount: String? = null,
        val Ipv6Addresses: List<EC2.NetworkInterface.Ipv6AddressProperty>? = null,
        val PrivateIpAddress: String? = null,
        val PrivateIpAddresses: List<EC2.NetworkInterface.PrivateIPSpecificationProperty>? = null,
        val SecondaryPrivateIpAddressCount: String? = null,
        val SourceDestCheck: String? = null,
        val SubnetId: String,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties {

        data class Ipv6AddressProperty(
            val Ipv6Address: String
        ) 


        data class PrivateIPSpecificationProperty(
            val PrivateIpAddress: String,
            val Primary: String
        ) 

    }

    @CloudFormationType("AWS::EC2::NetworkInterfaceAttachment")
    data class NetworkInterfaceAttachment(
        val DeleteOnTermination: String? = null,
        val DeviceIndex: String,
        val InstanceId: String,
        val NetworkInterfaceId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::PlacementGroup")
    data class PlacementGroup(
        val Strategy: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::Route")
    data class Route(
        val DestinationCidrBlock: String? = null,
        val DestinationIpv6CidrBlock: String? = null,
        val GatewayId: String? = null,
        val InstanceId: String? = null,
        val NatGatewayId: String? = null,
        val NetworkInterfaceId: String? = null,
        val RouteTableId: String,
        val VpcPeeringConnectionId: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::RouteTable")
    data class RouteTable(
        val VpcId: String,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::SecurityGroup")
    data class SecurityGroup(
        val GroupDescription: String,
        val SecurityGroupEgress: List<EC2.SecurityGroup.RuleProperty>? = null,
        val SecurityGroupIngress: List<EC2.SecurityGroup.RuleProperty>? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val VpcId: String? = null
    ) : ResourceProperties {

        data class RuleProperty(
            val CidrIp: String? = null,
            val DestinationPrefixListId: String? = null,
            val DestinationSecurityGroupId: String? = null,
            val FromPort: String? = null,
            val IpProtocol: String,
            val SourceSecurityGroupId: String? = null,
            val SourceSecurityGroupName: String? = null,
            val SourceSecurityGroupOwnerId: String? = null,
            val ToPort: String? = null
        ) 

    }

    @CloudFormationType("AWS::EC2::SecurityGroupEgress")
    data class SecurityGroupEgress(
        val CidrIp: String? = null,
        val CidrIpv6: String? = null,
        val DestinationPrefixListId: String? = null,
        val DestinationSecurityGroupId: String? = null,
        val FromPort: String,
        val GroupId: String,
        val IpProtocol: String,
        val ToPort: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::SecurityGroupIngress")
    data class SecurityGroupIngress(
        val CidrIp: String? = null,
        val CidrIpv6: String? = null,
        val FromPort: String? = null,
        val GroupId: String? = null,
        val GroupName: String? = null,
        val IpProtocol: String,
        val SourceSecurityGroupId: String? = null,
        val SourceSecurityGroupName: String? = null,
        val SourceSecurityGroupOwnerId: String? = null,
        val ToPort: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::SpotFleet")
    data class SpotFleet(
        val SpotFleetRequestConfigData: SpotFleet.SpotFleetRequestConfigDatumProperty
    ) : ResourceProperties {

        data class SpotFleetRequestConfigDatumProperty(
            val AllocationStrategy: String? = null,
            val ExcessCapacityTerminationPolicy: String? = null,
            val IamFleetRole: String,
            val LaunchSpecifications: List<EC2.SpotFleet.SpotFleetRequestConfigDatumProperty.LaunchSpecificationProperty>,
            val SpotPrice: String,
            val TargetCapacity: String,
            val TerminateInstancesWithExpiration: String? = null,
            val ValidFrom: String? = null,
            val ValidUntil: String? = null
        ) {

            data class LaunchSpecificationProperty(
                val BlockDeviceMappings: List<EC2.SpotFleet.SpotFleetRequestConfigDatumProperty.LaunchSpecificationProperty.BlockDeviceMappingProperty>? = null,
                val EbsOptimized: String? = null,
                val IamInstanceProfile: SpotFleet.SpotFleetRequestConfigDatumProperty.LaunchSpecificationProperty.IamInstanceProfileProperty? = null,
                val ImageId: String,
                val InstanceType: String,
                val KernelId: String? = null,
                val KeyName: String? = null,
                val Monitoring: SpotFleet.SpotFleetRequestConfigDatumProperty.LaunchSpecificationProperty.MonitoringProperty? = null,
                val NetworkInterfaces: List<EC2.SpotFleet.SpotFleetRequestConfigDatumProperty.LaunchSpecificationProperty.NetworkInterfaceProperty>? = null,
                val Placement: SpotFleet.SpotFleetRequestConfigDatumProperty.LaunchSpecificationProperty.PlacementProperty? = null,
                val RamdiskId: String? = null,
                val SecurityGroups: List<EC2.SpotFleet.SpotFleetRequestConfigDatumProperty.LaunchSpecificationProperty.SecurityGroupProperty>? = null,
                val SpotPrice: String? = null,
                val SubnetId: String? = null,
                val UserData: String? = null,
                val WeightedCapacity: String? = null
            ) {

                data class BlockDeviceMappingProperty(
                    val DeviceName: String,
                    val Ebs: SpotFleet.SpotFleetRequestConfigDatumProperty.LaunchSpecificationProperty.BlockDeviceMappingProperty.EbProperty? = null,
                    val NoDevice: String? = null,
                    val VirtualName: String? = null
                ) {

                    data class EbProperty(
                        val DeleteOnTermination: String? = null,
                        val Encrypted: String? = null,
                        val Iops: String? = null,
                        val SnapshotId: String? = null,
                        val VolumeSize: String? = null,
                        val VolumeType: String? = null
                    ) 

                }


                data class IamInstanceProfileProperty(
                    val Arn: String? = null
                ) 


                data class MonitoringProperty(
                    val Enabled: String? = null
                ) 


                data class NetworkInterfaceProperty(
                    val AssociatePublicIpAddress: String? = null,
                    val DeleteOnTermination: String? = null,
                    val Description: String? = null,
                    val DeviceIndex: String,
                    val Groups: List<String>? = null,
                    val Ipv6AddressCount: String? = null,
                    val Ipv6Addresses: List<EC2.NetworkInterface.Ipv6AddressProperty>? = null,
                    val NetworkInterfaceId: String? = null,
                    val PrivateIpAddresses: List<EC2.SpotFleet.SpotFleetRequestConfigDatumProperty.LaunchSpecificationProperty.NetworkInterfaceProperty.PrivateIpAddressProperty>? = null,
                    val SecondaryPrivateIpAddressCount: String? = null,
                    val SubnetId: String? = null
                ) {

                    data class PrivateIpAddressProperty(
                        val Primary: String? = null,
                        val PrivateIpAddress: String
                    ) 

                }


                data class PlacementProperty(
                    val AvailabilityZone: String? = null,
                    val GroupName: String? = null
                ) 


                data class SecurityGroupProperty(
                    val GroupId: String? = null
                ) 

            }

        }

    }

    @CloudFormationType("AWS::EC2::Subnet")
    data class Subnet(
        val AvailabilityZone: String? = null,
        val CidrBlock: String,
        val MapPublicIpOnLaunch: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val VpcId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::SubnetCidrBlock")
    data class SubnetCidrBlock(
        val Ipv6CidrBlock: String,
        val SubnetId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::SubnetNetworkAclAssociation")
    data class SubnetNetworkAclAssociation(
        val SubnetId: String,
        val NetworkAclId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::SubnetRouteTableAssociation")
    data class SubnetRouteTableAssociation(
        val RouteTableId: String,
        val SubnetId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::Volume")
    data class Volume(
        val AutoEnableIO: String? = null,
        val AvailabilityZone: String,
        val Encrypted: String? = null,
        val Iops: String? = null,
        val KmsKeyId: String? = null,
        val Size: String? = null,
        val SnapshotId: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val VolumeType: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VolumeAttachment")
    data class VolumeAttachment(
        val Device: String,
        val InstanceId: String,
        val VolumeId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPC")
    data class VPC(
        val CidrBlock: String,
        val EnableDnsSupport: String? = null,
        val EnableDnsHostnames: String? = null,
        val InstanceTenancy: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPCCidrBlock")
    data class VPCCidrBlock(
        val AmazonProvidedIpv6CidrBlock: String? = null,
        val VpcId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPCDHCPOptionsAssociation")
    data class VPCDHCPOptionsAssociation(
        val DhcpOptionsId: String,
        val VpcId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPCEndpoint")
    data class VPCEndpoint(
        val PolicyDocument: Any? = null,
        val RouteTableIds: List<String>? = null,
        val ServiceName: String,
        val VpcId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPCGatewayAttachment")
    data class VPCGatewayAttachment(
        val InternetGatewayId: String? = null,
        val VpcId: String,
        val VpnGatewayId: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPCPeeringConnection")
    data class VPCPeeringConnection(
        val PeerVpcId: String,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val VpcId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPNConnection")
    data class VPNConnection(
        val Type: String,
        val CustomerGatewayId: String,
        val StaticRoutesOnly: String? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val VpnGatewayId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPNConnectionRoute")
    data class VPNConnectionRoute(
        val DestinationCidrBlock: String,
        val VpnConnectionId: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPNGateway")
    data class VPNGateway(
        val Type: String,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::EC2::VPNGatewayRoutePropagation")
    data class VPNGatewayRoutePropagation(
        val RouteTableIds: List<String>,
        val VpnGatewayId: String
    ) : ResourceProperties 


}