package uy.kohesive.iac.util.aws

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import com.amazonaws.services.ec2.model.*
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder
import com.amazonaws.services.identitymanagement.model.*
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.buildSequence

class Ec2Crawl(parallelism: Int = Runtime.getRuntime().availableProcessors() * 16) {
    val parallelism = parallelism.coerceAtLeast(4).coerceAtMost(64)
    val baseContext = newFixedThreadPoolContext(parallelism, "AWS-crawl-pool")

    data class RegionClient<T : Any>(val region: String, val client: T)

    fun <T : Any> List<RegionClient<AmazonEC2>>.perClient(f: AmazonEC2.() -> List<T>): Deferred<Map<String, List<T>>> = async(baseContext) {
        val workers = this@perClient.map { ec2 ->
            async(context) {
                ec2.client.f().map { Pair(ec2.region, it) }
            }
        }
        workers.map { it.await() }.flatten().groupBy { it.first }.mapValues { it.value.map { it.second } }
    }

    fun <T : Any> List<RegionClient<AmazonEC2>>.perClientPaged(f: AmazonEC2.(nextToken: String?) -> Pair<String?, List<T>>): Deferred<Map<String, List<T>>> = async(baseContext) {
        val workers = this@perClientPaged.map { ec2 ->
            async(context) {
                buildSequence {
                    var pagingToken: String? = null
                    paging@ while (true) {
                        val (nextToken, results) = ec2.client.f(pagingToken)
                        yieldAll(results)
                        pagingToken = nextToken ?: break@paging
                    }
                }.map { Pair(ec2.region, it) }.toList() // force consume
            }
        }
        workers.map { it.await() }.flatten().groupBy { it.first }.mapValues { it.value.map { it.second } }
    }

    fun <T : Any> RegionClient<AmazonIdentityManagement>.paged(f: AmazonIdentityManagement.(nextToken: String?) -> Pair<String?, List<T>>): Deferred<List<T>> = async(baseContext) {
        buildSequence {
            var pagingToken: String? = null
            paging@ while (true) {
                val (nextToken, results) = client.f(pagingToken)
                yieldAll(results)
                pagingToken = nextToken ?: break@paging
            }
        }.toList() // force consume
    }


    fun go() {
        val usEast1Region = "us-east-1"

        runBlocking<Unit> {
            val credentials = ProfileCredentialsProvider("collokia-all")

            val ec2UsEast1 = RegionClient(usEast1Region, AmazonEC2ClientBuilder.standard().withCredentials(credentials).withRegion(usEast1Region).build())
            val stsUsEast1 = RegionClient(usEast1Region, AWSSecurityTokenServiceClientBuilder.standard().withCredentials(credentials).withRegion(usEast1Region).build())
            val iamUsEast1 = RegionClient(usEast1Region, AmazonIdentityManagementClientBuilder.standard().withCredentials(credentials).withRegion(usEast1Region).build())


            println("Fetching Account ID...")
            val awsAccountIdTask = async(baseContext) { stsUsEast1.client.getCallerIdentity(GetCallerIdentityRequest()).account }

            println("Fetching AWS Regions...")
            val ec2RegionsTask = async(baseContext) { ec2UsEast1.client.describeRegions().regions }

            val awsAccountId = awsAccountIdTask.await()
            val ec2Regions = ec2RegionsTask.await()

            println("Making clients per Region...")
            val ec2All = ec2Regions.map { region -> RegionClient(region.regionName, AmazonEC2ClientBuilder.standard().withCredentials(credentials).withRegion(region.regionName).build()) }
            val ec2ByRegion = ec2All.map { it.region to it }.toMap()

            println("Fetching EC2 instance list from all Regions...")
            val ec2InstancesTask = ec2All.perClientPaged { pagingToken ->
                val results = describeInstances(DescribeInstancesRequest().apply {
                    maxResults = 1000
                    nextToken = pagingToken
                })
                Pair(results.nextToken, results.reservations.map { it.instances }.flatten())
            }

            println("Fetching Owned AMI Images...")
            val ec2OwnedAMIImagesTask = ec2All.perClient {
                describeImages(DescribeImagesRequest().apply {
                    owners.add("self")
                }).images
            }

            println("Fetching Executable AMI Images...")
            val ec2ExecAmiImagesTask = ec2All.perClient {
                describeImages(DescribeImagesRequest().apply {
                    setExecutableUsers(listOf(awsAccountId))
                }).images
            }

            println("Fetching Security Group List...")
            val ec2SecurityGroupsTask = ec2All.perClient { describeSecurityGroups().securityGroups }

            println("Fetching Availability Zones from all Regions...")
            val ec2AvailZoneTask = ec2All.perClient { describeAvailabilityZones().availabilityZones }

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

            println("Fetching Internet Gateways...")
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
            val ec2VpcsTask = ec2All.perClient { describeVpcs().vpcs }

            println("Fetching VPC Peering connections...")
            val ec2VpcPeeringConnectionsTask = ec2All.perClient { describeVpcPeeringConnections().vpcPeeringConnections }

            println("Fetching VPN Gateways...")
            val ec2VpnGatewaysTask = ec2All.perClient { describeVpnGateways().vpnGateways }

            println("Fetching VPN Connections...")
            val ec2VpnConnectionsTask = ec2All.perClient { describeVpnConnections().vpnConnections }

            println("Fetching Account Attributes...")
            val ec2AccountInfoTask = listOf(ec2UsEast1).perClient { describeAccountAttributes().accountAttributes }

            // gather EC2 stuff...

            val ec2AvailZones = ec2AvailZoneTask.await()
            val ec2Instances = ec2InstancesTask.await()
            val ec2OwnedAmiImages = ec2OwnedAMIImagesTask.await()
            val ec2ExecAmiImages = ec2ExecAmiImagesTask.await()

            val ec2LeftoverImageIds = ec2Instances.map { region -> region.value.map { Pair(region.key, it.imageId) } }.flatten().toSet() -
                    ec2OwnedAmiImages.map { region -> region.value.map { Pair(region.key, it.imageId) } }.flatten().toSet() -
                    ec2ExecAmiImages.map { region -> region.value.map { Pair(region.key, it.imageId) } }.flatten().toSet()
            val ec2ReferencedImagesByIds = ec2LeftoverImageIds.groupBy { it.first }.mapValues { it.value.map { it.second } }

            println("Fetching Unknown AMI images that are not in account nor exec privileged...")
            val ec2UnknownAmiImagesTask: Deferred<Map<String, List<Image>>> = async(baseContext) {
                val tasks = ec2ReferencedImagesByIds.map { Pair(ec2ByRegion.get(it.key)!!, it.value) }.map { (client, imageIds) ->
                    listOf(client).perClient {
                        describeImages(DescribeImagesRequest().apply {
                            setImageIds(imageIds)
                        }).images
                    }
                }
                tasks.map { it.await() }.map { it.map { region -> region.value.map { region.key to it } }.flatten() }.flatten().groupBy { it.first }.mapValues { it.value.map { it.second } }
            }

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
            val ec2Vpcs = ec2VpcsTask.await()
            val ec2VpcPeeringConnections = ec2VpcPeeringConnectionsTask.await()
            val ec2VpnGateways = ec2VpnGatewaysTask.await()
            val ec2VpnConnections = ec2VpnConnectionsTask.await()
            val ec2AccountAttrs = ec2AccountInfoTask.await().map { it.value }.first()
            val ec2ReferencedAmiImages = ec2UnknownAmiImagesTask.await()

            // continue onto IAM ...

            println("Fetching IAM Users...")
            val iamUsersTask = iamUsEast1.paged { pagingToken ->
                val results = listUsers(ListUsersRequest().apply {
                    maxItems = 1000
                    marker = pagingToken
                })
                Pair(if (results.isTruncated()) results.marker else null, results.users)
            }

            println("Fetching IAM Groups...")
            val iamGroupsTask = iamUsEast1.paged { pagingToken ->
                val results = listGroups(ListGroupsRequest().apply {
                    maxItems = 1000
                    marker = pagingToken
                })
                Pair(if (results.isTruncated()) results.marker else null, results.groups)
            }

            println("Fetching IAM Roles...")
            val iamRolesTask = iamUsEast1.paged { pagingToken ->
                val results = listRoles(ListRolesRequest().apply {
                    maxItems = 1000
                    marker = pagingToken
                })
                Pair(if (results.isTruncated()) results.marker else null, results.roles)
            }

            println("Fetching IAM Policies...")
            val iamPoliciesTask = iamUsEast1.paged { pagingToken ->
                val results = listPolicies(ListPoliciesRequest().apply {
                    maxItems = 1000
                    marker = pagingToken
                })
                Pair(if (results.isTruncated()) results.marker else null, results.policies)
            }

            // gather IAM stuff...

            val iamUserList = iamUsersTask.await()

            println("Fetching IAM Groups for each User...")
            val iamUserGroupsTask = iamUserList.map { user ->
                val groups = iamUsEast1.paged { pagingToken ->
                    val results = listGroupsForUser(ListGroupsForUserRequest().apply {
                        maxItems = 1000
                        marker = pagingToken
                        userName = user.userName
                    })
                    Pair(if (results.isTruncated()) results.marker else null, results.groups)
                }
                Pair(user, groups)
            }

            println("Fetching IAM Access keys for each User...")
            val iamUserAccessKeysTask = iamUserList.map { user ->
                val keys = iamUsEast1.paged { pagingToken ->
                    val results = listAccessKeys(ListAccessKeysRequest().apply {
                        maxItems = 1000
                        marker = pagingToken
                        userName = user.userName
                    })
                    Pair(if (results.isTruncated()) results.marker else null, results.accessKeyMetadata)
                }
                Pair(user, keys)
            }

            println("Fetching IAM Attached Policy for each User...")
            val iamUserAttachedPoliciesTask = iamUserList.map { user ->
                val keys = iamUsEast1.paged { pagingToken ->
                    val results = listAttachedUserPolicies(ListAttachedUserPoliciesRequest().apply {
                        maxItems = 1000
                        marker = pagingToken
                        userName = user.userName
                    })
                    Pair(if (results.isTruncated()) results.marker else null, results.attachedPolicies)
                }
                Pair(user, keys)
            }

            println("Fetching IAM Embedded Policy for each User...")
            val iamUserEmbeddedPoliciesTask = iamUserList.map { user ->
                val keys = iamUsEast1.paged { pagingToken ->
                    val results = listUserPolicies(ListUserPoliciesRequest().apply {
                        maxItems = 1000
                        marker = pagingToken
                        userName = user.userName
                    })
                    Pair(if (results.isTruncated()) results.marker else null, results.policyNames)
                }
                Pair(user, keys)
            }

            val iamGroupList = iamGroupsTask.await()

            println("Fetching IAM Embedded Policy for each Group...")
            val iamGroupEmbeddedPoliciesTask = iamGroupList.map { group ->
                val keys = iamUsEast1.paged { pagingToken ->
                    val results = listGroupPolicies(ListGroupPoliciesRequest().apply {
                        maxItems = 1000
                        marker = pagingToken
                        groupName = group.groupName
                    })
                    Pair(if (results.isTruncated()) results.marker else null, results.policyNames)
                }
                Pair(group, keys)
            }

            println("Fetching IAM Attached Policy for each Group...")
            val iamGroupAttachedPoliciesTask = iamGroupList.map { group ->
                val keys = iamUsEast1.paged { pagingToken ->
                    val results = listAttachedGroupPolicies(ListAttachedGroupPoliciesRequest().apply {
                        maxItems = 1000
                        marker = pagingToken
                        groupName = group.groupName
                    })
                    Pair(if (results.isTruncated()) results.marker else null, results.attachedPolicies)
                }
                Pair(group, keys)
            }

            val iamRoleList = iamRolesTask.await()

            println("Fetching IAM Embedded Policy for each Role...")
            val iamRoleEmbeddedPoliciesTask = iamRoleList.map { role ->
                val keys = iamUsEast1.paged { pagingToken ->
                    val results = listRolePolicies(ListRolePoliciesRequest().apply {
                        maxItems = 1000
                        marker = pagingToken
                        roleName = role.roleName
                    })
                    Pair(if (results.isTruncated()) results.marker else null, results.policyNames)
                }
                Pair(role, keys)
            }

            println("Fetching IAM Attached Policy for each Role...")
            val iamRoleAttachedPoliciesTask = iamRoleList.map { role ->
                val keys = iamUsEast1.paged { pagingToken ->
                    val results = listAttachedRolePolicies(ListAttachedRolePoliciesRequest().apply {
                        maxItems = 1000
                        marker = pagingToken
                        roleName = role.roleName
                    })
                    Pair(if (results.isTruncated()) results.marker else null, results.attachedPolicies)
                }
                Pair(role, keys)
            }


            val iamPolicyList = iamPoliciesTask.await()

            /* we already get this from the user, group, role lookups
            println("Fetching IAM attached Entites for each Policy...")
            val iamPolicyAttachedEntitiesTask = iamPolicyList.map { policy ->
                val keys = iamUsEast1.paged { pagingToken ->
                    val results = listEntitiesForPolicy(ListEntitiesForPolicyRequest().apply {
                        maxItems = 1000
                        marker = pagingToken
                        policyArn = policy.arn
                    })
                    Pair(if (results.isTruncated()) results.marker else null, results.)
                }
                Pair(policy, keys)
            }
            */

            val iamUserArnToGroups = iamUserGroupsTask.map { it.first.arn to it.second.await() }.toMap()
            val iamUserArnToAccessKeys = iamUserAccessKeysTask.map { it.first.arn to it.second.await() }.toMap()
            val iamUserArnToAttachedPolicies = iamUserAttachedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
            val iamUserArnToEmbeddedPolicies = iamUserEmbeddedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
            val iamGroupArnToAttachedPolicies = iamGroupAttachedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
            val iamGroupArnToEmbeddedPolicies = iamGroupEmbeddedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
            val iamRoleArnToAttachedPolicies = iamRoleAttachedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
            val iamRoleArnToEmbeddedPolicies = iamRoleEmbeddedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()

            // print all results:

            fun List<Tag>.nameTag() = firstOrNull { it.key.equals("Name", ignoreCase = true) }?.value ?: "unknown"

            println("============[ EC2 RESULTS ]=============")

            println("Account attributes:")
            ec2AccountAttrs.forEach {
                println("  ${it.attributeName} = ${it.attributeValues.map { it.attributeValue }.joinToString()}")
            }

            println()
            println("Regions:")
            ec2Regions.forEach {
                println("  ${it.regionName} :: ${it.endpoint}")
            }

            println()
            println("Availability zones:")
            ec2AvailZones.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.regionName} :: ${it.zoneName}")
                }
            }

            println()
            println("EC2 Instances:")
            ec2Instances.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.tags.nameTag()} :: ${it.instanceId} :: ${it.imageId} :: ${it.keyName} :: ${it.securityGroups.map { it.groupName }.joinToString()}")
                }
            }

            println()
            println("EC2 Instance Profiles Assoc:")
            ec2InstanceProfileAssoc.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.instanceId} :: ${it.iamInstanceProfile.id} :: ${it.iamInstanceProfile.arn}")
                }
            }

            println()
            println("EC2 KeyPairs:")
            ec2KeyPairs.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.keyName} :: ${it.keyFingerprint}")
                }
            }

            println()
            println("Security Groups:")
            ec2SecurityGroups.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.groupName} :: ${it.groupId} :: ${it.vpcId}")
                }
            }

            println()
            println("Elastic IPs:")
            ec2ElasticIps.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.publicIp} :: ${it.privateIpAddress} :: ${it.instanceId}")
                }
            }

            println()
            println("VPCs:")
            ec2Vpcs.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.tags.nameTag()} :: ${it.vpcId}")
                }
            }

            println()
            println("VPC Peering:")
            ec2VpcPeeringConnections.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.tags.nameTag()} :: ${it.vpcPeeringConnectionId} :: ${it.accepterVpcInfo.vpcId} <- ${it.requesterVpcInfo.vpcId}")
                }
            }

            println()
            println("EC2 Subnets:")
            ec2Subnets.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.tags.nameTag()} :: ${it.vpcId} :: ${it.subnetId} :: ${it.cidrBlock}")
                }
            }

            println()
            println("EC2 Owned AMI Images:")
            ec2OwnedAmiImages.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.name ?: it.tags.nameTag()} :: ${it.imageId} :: ${it.architecture} :: ${it.imageType} :: ${it.kernelId} :: ${it.description}")
                }
            }

            println()
            println("EC2 Execute Privileged AMI Images:")
            ec2ExecAmiImages.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.name ?: it.tags.nameTag()} :: ${it.imageId} :: ${it.architecture} :: ${it.imageType} :: ${it.kernelId} :: ${it.description}")
                }
            }

            println()
            println("EC2 Other Referenced AMI Images:")
            ec2ReferencedAmiImages.forEach {
                println("  ${it.key}:")
                it.value.forEach {
                    println("    ${it.name ?: it.tags.nameTag()} :: ${it.imageId} :: ${it.architecture} :: ${it.imageType} :: ${it.kernelId} :: ${it.description}")
                }
            }

            println()
            println("============[ IAM RESULTS ]=============")
            println()

            println("IAM User List:")
            iamUserList.forEach {
                println("  ${it.userName} :: ${it.userId} :: ${it.arn} :: ")
                println("       groups:        ${iamUserArnToGroups.get(it.arn)?.map { it.groupName }?.joinToString() ?: "n/a"}")
                println("       keys:          ${iamUserArnToAccessKeys.get(it.arn)?.map { it.accessKeyId }?.joinToString() ?: "n/a"}")
                println("       policies:      ${iamUserArnToEmbeddedPolicies.get(it.arn)?.joinToString() ?: "n/a"}")
                println("       ext policies:  ${iamUserArnToAttachedPolicies.get(it.arn)?.map { it.policyName }?.joinToString() ?: "n/a"}")
            }

            println()
            println("IAM Group List:")
            iamGroupList.forEach {
                println("  ${it.groupName} :: ${it.groupId} :: ${it.arn}")
                println("       policies:      ${iamGroupArnToEmbeddedPolicies.get(it.arn)?.joinToString() ?: "n/a"}")
                println("       ext policies:  ${iamGroupArnToAttachedPolicies.get(it.arn)?.map { it.policyName }?.joinToString() ?: "n/a"}")
            }

            println()
            println("IAM Role List:")
            iamRoleList.forEach {
                println("  ${it.roleName} :: ${it.roleId} :: ${it.arn}")
                println("       policies:      ${iamRoleArnToEmbeddedPolicies.get(it.arn)?.joinToString() ?: "n/a"}")
                println("       ext policies:  ${iamRoleArnToAttachedPolicies.get(it.arn)?.map { it.policyName }?.joinToString() ?: "n/a"}")

            }

            println()
            println("IAM Policy List:")
            iamPolicyList.forEach {
                println("  ${it.policyName} :: ${it.policyId} :: ${it.arn}")
            }

            println()
            println("done.")
        }
    }
}

fun main(args: Array<String>) {
    println("AWS Crawler")

    Ec2Crawl().go()
}