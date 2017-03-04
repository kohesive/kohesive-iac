package uy.kohesive.iac.util.aws

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.buildSequence
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

class Ec2Crawl {

    fun <T : Any> List<AmazonEC2>.perClient(f: AmazonEC2.() -> List<T>): Deferred<List<T>> = async(CommonPool) {
        val workers = this@perClient.map { ec2 ->
            async(context) {
                ec2.f()
            }
        }
        workers.map { it.await() }.flatten()
    }

    inline fun <T: Any> List<AmazonEC2>.perClientPaged(crossinline requestFunc: AmazonEC2.(nextToken: String?)->Pair<String?, List<T>>) = async(CommonPool) {
        val workers = this@perClientPaged.map { ec2 ->
            async(context) {
                buildSequence {
                    var pagingToken: String? = null
                    paging@ while (true) {
                        val (nextToken, results) = ec2.requestFunc(pagingToken)
                        yieldAll(results)
                        pagingToken = nextToken ?: break@paging
                    }
                }.toList() // force consume
            }
        }
        workers.map { it.await() }.flatten()
    }


    fun go() = runBlocking<Unit> {
        val credentials = ProfileCredentialsProvider("collokia-all")

        val ec2UsEast1 = AmazonEC2Client.builder().withCredentials(credentials).withRegion("us-east-1").build()

        println("Fetching AWS Regions...")
        val ec2RegionsTask = listOf(ec2UsEast1).perClient { describeRegions().regions }

        val ec2Regions = ec2RegionsTask.await()

        println("Making clients per Region...")
        val ec2All = ec2Regions.map { AmazonEC2Client.builder().withCredentials(credentials).withRegion(it.regionName).build() }

        println("Fetching EC2 instance list from all Regions...")
        val ec2InstancesTask = ec2All.perClientPaged { pagingToken ->
            val results = describeInstances(DescribeInstancesRequest().apply {
                maxResults = 1000
                nextToken = pagingToken
            })
            Pair(results.nextToken, results.reservations.map { it.instances }.flatten())
        }

        println("Fetching Security Group List...")
        val ec2SecurityGroupsTask = ec2All.perClient { describeSecurityGroups().securityGroups }

        println("Fetching Availability Zones from all Regions...")
        val ec2AvailZoneTask =  ec2All.perClient { describeAvailabilityZones().availabilityZones }

        println("Fetching Elastic IP Addresses...")
        val ec2ElasticIpsTask = ec2All.perClient { describeAddresses().addresses }

        println("Fetching SSH KeyPairs...")
        val ec2KeyPairsTask = ec2All.perClient { describeKeyPairs().keyPairs }

        println("Fetching Instance Profile associatinos...")
        val ec2InstanceProfilesTask = ec2All.perClientPaged { pagingToken ->
            val results = describeIamInstanceProfileAssociations(DescribeIamInstanceProfileAssociationsRequest().apply {
                maxResults = 1000
                nextToken = pagingToken
            })
            Pair(results.nextToken, results.iamInstanceProfileAssociations)
        }

        println("Fetching Customer Gateways...")
        val ec2CustomerGatewaysTask = ec2All.perClient { describeCustomerGateways().customerGateways }

        println("Fetching Egress Only Internet Gateways...")
        val ec2EgressOnlyGatewaysTask = ec2All.perClientPaged { pagingToken ->
            val results = describeEgressOnlyInternetGateways(DescribeEgressOnlyInternetGatewaysRequest().apply {
                maxResults = 255
                nextToken = pagingToken
            })
            Pair(results.nextToken, results.egressOnlyInternetGateways)
        }

        println("Fetching Intermet Gateways...")
        val ec2InternetGatewaysTask = ec2All.perClient { describeInternetGateways().internetGateways }

        println("Fetching NAT Gateways...")
        val ec2NatGatewaysTask = ec2All.perClientPaged { pagingToken ->
            val results = describeNatGateways(DescribeNatGatewaysRequest().apply {
                maxResults = 1000
                nextToken = pagingToken
            })
            Pair(results.nextToken, results.natGateways)
        }

        println("Fetching Network ACLs...")
        val ec2NetworkAclsTask = ec2All.perClient { describeNetworkAcls().networkAcls }

        println("Fetching Network Interfaces...")
        val ec2NetworkInterfacesTask = ec2All.perClient { describeNetworkInterfaces().networkInterfaces }

        println("Fetching Route Tables...")
        val ec2RouteTablesTask = ec2All.perClient { describeRouteTables().routeTables }

        println("Fetching Subnets...")
        val ec2SubnetsTask = ec2All.perClient { describeSubnets().subnets }

        println("Fetching VPCs...")
        val ec2VPCsTask = ec2All.perClient { describeVpcs().vpcs }

        println("Fetching VPC Peering connections...")
        val ec2VPCPeeringConnectionsTask = ec2All.perClient { describeVpcPeeringConnections().vpcPeeringConnections }

        println("Fetching VPN Gateways...")
        val ec2VPNGatewaysTask = ec2All.perClient { describeVpnGateways().vpnGateways }

        println("Fetching VPN Connections...")
        val ec2VPNVpnConnectionsTask = ec2All.perClient { describeVpnConnections().vpnConnections }


        // gather...

        val ec2AvailZones = ec2AvailZoneTask.await()
        val ec2Instances = ec2InstancesTask.await()
        val ec2SecurityGroups = ec2SecurityGroupsTask.await()
        val ec2ElasticIps = ec2ElasticIpsTask.await()
        val ec2KeyPairs = ec2KeyPairsTask.await()
        val ec2InstanceProfileAssoc = ec2InstanceProfilesTask.await()
        val ec2CustomerGateways = ec2CustomerGatewaysTask.await()
        val ec2EgressOnlyGateways = ec2EgressOnlyGatewaysTask.await()
        val ec2InternetGateways = ec2InternetGatewaysTask.await()
        val ec2NatGateways = ec2NatGatewaysTask.await()
        val ec2NetworkAcls = ec2NetworkAclsTask.await()
        val ec2NetworkInterfaces = ec2NetworkInterfacesTask.await()
        val ec2RouteTables = ec2RouteTablesTask.await()
        val ec2Subnets = ec2SubnetsTask.await()
        val ec2VPCs = ec2VPCsTask.await()
        val ec2VPCPeeringConnections = ec2VPCPeeringConnectionsTask.await()
        val ec2VPNGateways = ec2VPNGatewaysTask.await()
        val ec2VPNConnections = ec2VPNVpnConnectionsTask.await()

        fun List<Tag>.nameTag() = firstOrNull { it.key.equals("Name", ignoreCase = true) }?.value ?: "unknown"

        println("============[ RESULTS ]=============")

        println()
        println("Regions:")
        ec2Regions.forEach {
            println("${it.regionName} :: ${it.endpoint}")
        }

        println()
        println("Availability zones:")
        ec2AvailZones.forEach {
            println("${it.regionName} :: ${it.zoneName}")
        }

        println()
        println("EC2 Instances:")
        ec2Instances.forEach {
            println("${it.tags.nameTag()} :: ${it.instanceId} :: ${it.keyName} :: ${it.securityGroups.map { it.groupName }.joinToString()}")
        }

        println()
        println("EC2 Instance Profiles Assoc:")
        ec2InstanceProfileAssoc.forEach {
            println("${it.instanceId} :: ${it.iamInstanceProfile.id} :: ${it.iamInstanceProfile.arn}")
        }

        println()
        println("EC2 KeyPairs:")
        ec2KeyPairs.forEach {
            println("${it.keyName} :: ${it.keyFingerprint}")
        }

        println()
        println("Security Groups:")
        ec2SecurityGroups.forEach {
            println("${it.groupName} :: ${it.groupId} :: ${it.vpcId}")
        }

        println()
        println("Elastic IPs:")
        ec2ElasticIps.forEach {
            println("${it.publicIp} :: ${it.privateIpAddress} :: ${it.instanceId}")
        }

        println()
        println("VPCs:")
        ec2VPCs.forEach {
            println("${it.tags.nameTag()} :: ${it.vpcId}")
        }

        println()
        println("VPC Peering:")
        ec2VPCPeeringConnections.forEach {
            println("${it.tags.nameTag()} :: ${it.vpcPeeringConnectionId} :: ${it.accepterVpcInfo.vpcId} <- ${it.requesterVpcInfo.vpcId}")
        }

        println()
        println("EC2 Subnets:")
        ec2Subnets.forEach {
            println("${it.tags.nameTag()} :: ${it.vpcId} :: ${it.subnetId} :: ${it.cidrBlock}")
        }

        println("done.")
    }
}

fun main(args: Array<String>) {
    println("Hello from Mono!")

    Ec2Crawl().go()
}