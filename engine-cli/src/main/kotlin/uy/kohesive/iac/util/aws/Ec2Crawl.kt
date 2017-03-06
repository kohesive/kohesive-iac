package uy.kohesive.iac.util.aws

import com.amazonaws.auth.*
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import com.amazonaws.services.ec2.model.*
import com.amazonaws.services.ec2.model.AvailabilityZone
import com.amazonaws.services.ec2.model.Subnet
import com.amazonaws.services.ec2.model.Tag
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder
import com.amazonaws.services.identitymanagement.model.*
import com.amazonaws.services.rds.AmazonRDSClientBuilder
import com.amazonaws.services.rds.model.*
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.buildSequence

data class AwsCrawlCredentials(private val basicCredentials: BasicAWSCredentials? = null,
                               private val profileName: String? = null) {
    val provider: AWSCredentialsProvider = run {
        val providers = listOf(
                basicCredentials?.let { AWSStaticCredentialsProvider(it) },
                profileName?.let { ProfileCredentialsProvider(profileName) },
                if (basicCredentials == null && profileName == null) InstanceProfileCredentialsProvider(true) else null
        ).filterNotNull()
        AWSCredentialsProviderChain(providers)
    }
}


class Ec2Crawl(credentialsCfg: AwsCrawlCredentials,
               parallelism: Int = Runtime.getRuntime().availableProcessors() * 16) {
    val parallelism = parallelism.coerceAtLeast(4).coerceAtMost(64)
    val baseContext = newFixedThreadPoolContext(parallelism, "AWS-crawl-pool")

    data class RegionClient<T : Any>(val region: String, val client: T)

    fun <T : Any, C : Any> List<RegionClient<C>>.perClient(f: C.() -> List<T>): Deferred<Map<String, List<T>>> = async(baseContext) {
        val workers = this@perClient.map { ec2 ->
            async(context) {
                ec2.client.f().map { Pair(ec2.region, it) }
            }
        }
        workers.map { it.await() }.flatten().groupBy { it.first }.mapValues { it.value.map { it.second } }
    }

    fun <T : Any, C : Any> List<RegionClient<C>>.perClientPaged(f: C.(nextToken: String?) -> Pair<String?, List<T>>): Deferred<Map<String, List<T>>> = async(baseContext) {
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

    fun <T : Any, C : Any> RegionClient<C>.paged(f: C.(nextToken: String?) -> Pair<String?, List<T>>): Deferred<List<T>> = async(baseContext) {
        buildSequence {
            var pagingToken: String? = null
            paging@ while (true) {
                val (nextToken, results) = client.f(pagingToken)
                yieldAll(results)
                pagingToken = nextToken ?: break@paging
            }
        }.toList() // force consume
    }

    val usEast1Region = "us-east-1"
    val credentials = credentialsCfg.provider

    fun fetchAccountId(): String = runBlocking<String> {
        val stsUsEast1 = RegionClient(usEast1Region, AWSSecurityTokenServiceClientBuilder.standard().withCredentials(credentials).withRegion(usEast1Region).build())
        println("Fetching Account ID...")
        val awsAccountIdTask = async(baseContext) { stsUsEast1.client.getCallerIdentity(GetCallerIdentityRequest()).account }
        awsAccountIdTask.await()
    }

    fun fetchAwsRegions(): List<Region> = runBlocking<List<Region>> {
        val ec2UsEast1 = RegionClient(usEast1Region, AmazonEC2ClientBuilder.standard().withCredentials(credentials).withRegion(usEast1Region).build())
        println("Fetching AWS Regions...")
        val ec2RegionsTask = async(baseContext) { ec2UsEast1.client.describeRegions().regions }
        ec2RegionsTask.await()
    }

    fun fetchEc2Data(awsAccountId: String, ec2Regions: List<Region>) = runBlocking<Ec2Data> {
        println("Making clients per Region...")
        val ec2UsEast1 = RegionClient(usEast1Region, AmazonEC2ClientBuilder.standard().withCredentials(credentials).withRegion(usEast1Region).build())
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

        Ec2Data(accountAttributes = ec2AccountAttrs,
                regions = ec2Regions.map { it.regionName },
                availabilityZonesByRegion = ec2AvailZones,
                instancesByRegion = ec2Instances,
                ownedAmisByRegion = ec2OwnedAmiImages,
                execAmisByRegion = ec2ExecAmiImages,
                otherAmisByRegion = ec2ReferencedAmiImages,
                securityGroupsByRegion = ec2SecurityGroups,
                elasticIpsByRegion = ec2ElasticIps,
                keyPairsByRegion = ec2KeyPairs,
                instanceProfileAssociationsByRegion = ec2InstanceProfileAssoc,
                clusterGatewaysByRegion = ec2CustomerGateways,
                egressOnlyInternetGatewaysByRegion = ec2EgressOnlyGateways,
                internetGatewaysByRegion = ec2InternetGateways,
                natGatewaysByRegion = ec2NatGateways,
                networkAclsByRegion = ec2NetworkAcls,
                networkInterfacesByRegion = ec2NetworkInterfaces,
                routeTablesByRegion = ec2RouteTables,
                subnetsByRegion = ec2Subnets,
                vpcsByRegion = ec2Vpcs,
                vpcPeeringConnectionsByRegion = ec2VpcPeeringConnections,
                vpnGatewaysByRegion = ec2VpnGateways,
                vpnConnectionsByRegion = ec2VpnConnections)
    }

    fun fetchIamData(awsAccountId: String, ec2Regions: List<Region>) = runBlocking<IamData> {
        val iamUsEast1 = RegionClient(usEast1Region, AmazonIdentityManagementClientBuilder.standard().withCredentials(credentials).withRegion(usEast1Region).build())

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
                this.setScope(PolicyScopeType.Local)
            })
            Pair(if (results.isTruncated()) results.marker else null, results.policies)
        }

        println("Fetching IAM Instance Profiles...")
        val iamInstanceProfilesTask = iamUsEast1.paged { pagingToken ->
            val results = listInstanceProfiles(ListInstanceProfilesRequest().apply {
                maxItems = 1000
                marker = pagingToken
            })
            Pair(if (results.isTruncated()) results.marker else null, results.instanceProfiles)
        }

        println("Fetching IAM Virtual MFA Devices...")
        val iamVirtMfaDevicesTask = iamUsEast1.paged { pagingToken ->
            val results = listVirtualMFADevices(ListVirtualMFADevicesRequest().apply {
                maxItems = 1000
                marker = pagingToken
                assignmentStatus = "Assigned"
            })
            Pair(if (results.isTruncated()) results.marker else null, results.virtualMFADevices)
        }

        // TODO:  Things not fetched:
        //        OpenIDConnectProviders
        //        SAMLProviders
        //        ServerCertificates
        //        ServiceSpecificCredentials
        //        SigningCertificates
        //        SSHPublicKeys
        //

        // gather IAM stuff, and fan out new 2nd level requests...

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

        println("Fetching IAM MFA Device for each User...")
        val iamUserMfaDevicesTask = iamUserList.map { user ->
            val keys = iamUsEast1.paged { pagingToken ->
                val results = listMFADevices(ListMFADevicesRequest().apply {
                    maxItems = 1000
                    marker = pagingToken
                    userName = user.userName
                })
                Pair(if (results.isTruncated()) results.marker else null, results.mfaDevices)
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

        println("Fetching IAM Instance Profile for each Role...")
        val iamRoleAssocInstanceProfilesTask = iamRoleList.map { role ->
            val keys = iamUsEast1.paged { pagingToken ->
                val results = listInstanceProfilesForRole(ListInstanceProfilesForRoleRequest().apply {
                    maxItems = 1000
                    marker = pagingToken
                    roleName = role.roleName
                })
                Pair(if (results.isTruncated()) results.marker else null, results.instanceProfiles)
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

        // gather second level IAM requests

        val iamInstanceProfiles = iamInstanceProfilesTask.await()
        val iamVirtMfaDevices = iamVirtMfaDevicesTask.await()
        val iamUserArnToGroups = iamUserGroupsTask.map { it.first.arn to it.second.await() }.toMap()
        val iamUserArnToAccessKeys = iamUserAccessKeysTask.map { it.first.arn to it.second.await() }.toMap()
        val iamUserArnToAttachedPolicies = iamUserAttachedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
        val iamUserArnToEmbeddedPolicies = iamUserEmbeddedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
        val iamUserArnToMfaDevices = iamUserMfaDevicesTask.map { it.first.arn to it.second.await() }.toMap()
        val iamUserArnToVirtMfaDevices = iamVirtMfaDevices.map { it.user.arn to it }.toMap()
        val iamGroupArnToAttachedPolicies = iamGroupAttachedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
        val iamGroupArnToEmbeddedPolicies = iamGroupEmbeddedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
        val iamRoleArnToAttachedPolicies = iamRoleAttachedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
        val iamRoleArnToEmbeddedPolicies = iamRoleEmbeddedPoliciesTask.map { it.first.arn to it.second.await() }.toMap()
        val iamRoleArnToAssocInstanceProfiles = iamRoleAssocInstanceProfilesTask.map { it.first.arn to it.second.await() }.toMap()

        IamData(users = iamUserList,
                groups = iamGroupList,
                policies = iamPolicyList,
                roles = iamRoleList,
                instanceProfiles = iamInstanceProfiles,
                virtMfaDevices = iamVirtMfaDevices,
                userGroupAssignmentsByUserArn = iamUserArnToGroups,
                userAccessKeysByUserArn = iamUserArnToAccessKeys,
                userAttachedPoliciesByUserArn = iamUserArnToAttachedPolicies,
                userEmbeddedPolicyNamesByUserArn = iamUserArnToEmbeddedPolicies,
                userMfaDevicesByUserArn = iamUserArnToMfaDevices,
                userVirtualMfaDevicesByUserArn = iamUserArnToVirtMfaDevices,
                groupAttachedPoliciesByGroupArn = iamGroupArnToAttachedPolicies,
                groupEmbeddedPolicyNamesByGroupArn = iamGroupArnToEmbeddedPolicies,
                roleAttachedPoliciesByRoleArn = iamRoleArnToAttachedPolicies,
                roleEmbeddedPolicyNamesByRoleArn = iamRoleArnToEmbeddedPolicies,
                roleAssociatedInstanceProfilesByRoleArn = iamRoleArnToAssocInstanceProfiles)

    }

    fun fetchRdsData(awsAccountId: String, ec2Regions: List<Region>) = runBlocking<RdsData> {
        val rdsUsEast1 = RegionClient(usEast1Region, AmazonRDSClientBuilder.standard().withCredentials(credentials).withRegion(usEast1Region).build())
        // val rdsAll = ec2Regions.map { region -> RegionClient(region.regionName, AmazonRDSClientBuilder.standard().withCredentials(credentials).withRegion(region.regionName).build()) }

        println("Fetching RDS Account quotas...")
        val accountQuotas = async(baseContext) { rdsUsEast1.client.describeAccountAttributes().accountQuotas }.await()

        println("Fetching RDS Certificates...")
        val rdsCertificatesTask = rdsUsEast1.paged { pagingToken ->
            val results = rdsUsEast1.client.describeCertificates(DescribeCertificatesRequest().apply {
                maxRecords = 100
                marker = pagingToken
            })
            Pair(results.marker, results.certificates)
        }

        println("Fetching RDS Clusters...")
        val rdsClustersTask = rdsUsEast1.paged { pagingToken ->
            val results = rdsUsEast1.client.describeDBClusters(DescribeDBClustersRequest().apply {
                maxRecords = 100
                marker = pagingToken
            })
            Pair(results.marker, results.dbClusters)
        }

        println("Fetching RDS Instances...")
        val rdsInstancesTask = rdsUsEast1.paged { pagingToken ->
            val results = rdsUsEast1.client.describeDBInstances(DescribeDBInstancesRequest().apply {
                maxRecords = 100
                marker = pagingToken
            })
            Pair(results.marker, results.dbInstances)
        }

        println("Fetching RDS Security Groups...")
        val rdsSecurityGroupsTask = rdsUsEast1.paged { pagingToken ->
            val results = rdsUsEast1.client.describeDBSecurityGroups(DescribeDBSecurityGroupsRequest().apply {
                maxRecords = 100
                marker = pagingToken
            })
            Pair(results.marker, results.dbSecurityGroups)
        }

        println("Fetching RDS Subnet Groups...")
        val rdsSubnetGroupsTask = rdsUsEast1.paged { pagingToken ->
            val results = rdsUsEast1.client.describeDBSubnetGroups(DescribeDBSubnetGroupsRequest().apply {
                maxRecords = 100
                marker = pagingToken
            })
            Pair(results.marker, results.dbSubnetGroups)
        }


        val rdsCertificates = rdsCertificatesTask.await()
        val rdsInstances = rdsInstancesTask.await()
        val rdsSecurityGroups = rdsSecurityGroupsTask.await()
        val rdsSubnetGroups = rdsSubnetGroupsTask.await()
        val rdsClusters = rdsClustersTask.await()

        RdsData(dbCertificates = rdsCertificates,
                dbClusters = rdsClusters,
                dbInstances = rdsInstances,
                dbSecurityGroups = rdsSecurityGroups,
                dbSubnetGroups = rdsSubnetGroups)

        /*
        rdsUsEast1.client.describeDBClusterParameterGroups()
        rdsUsEast1.client.describeDBClusterParameters()
        rdsUsEast1.client.describeDBClusterSnapshots()
        rdsUsEast1.client.describeDBClusterSnapshotAttributes()
        rdsUsEast1.client.describeDBEngineVersions()
        rdsUsEast1.client.describeDBParameterGroups()
        rdsUsEast1.client.describeDBParameters()
        rdsUsEast1.client.describeDBSnapshots()
        rdsUsEast1.client.describeDBSnapshotAttributes()
        rdsUsEast1.client.describeEngineDefaultClusterParameters()
        rdsUsEast1.client.describeEngineDefaultParameters()
        rdsUsEast1.client.describeEventCategories()
        rdsUsEast1.client.describeEventSubscriptions()
        rdsUsEast1.client.describeEvents()
        rdsUsEast1.client.describeOptionGroupOptions()
        rdsUsEast1.client.describeOptionGroups()
        rdsUsEast1.client.describeOrderableDBInstanceOptions()
        rdsUsEast1.client.describePendingMaintenanceActions()
        rdsUsEast1.client.describeSourceRegions()
        */


    }

    fun fetchAllData() = runBlocking<AwsData> {
        val accountId = fetchAccountId()
        val regions = fetchAwsRegions()

        val ec2Data = fetchEc2Data(accountId, regions)
        val iamData = fetchIamData(accountId, regions)
        val rdsData = fetchRdsData(accountId, regions)

        printEc2Data(ec2Data)
        printIamData(iamData)
        printRdsData(rdsData)

        AwsData(accountId, ec2Data, iamData, rdsData)
    }
}

data class AwsData(val accountId: String,
                   val ec2Data: Ec2Data,
                   val iamData: IamData,
                   val rdsData: RdsData)

data class Ec2Data(val accountAttributes: List<AccountAttribute>,
                   val regions: List<String>,
                   val availabilityZonesByRegion: Map<String, List<AvailabilityZone>>,
                   val instancesByRegion: Map<String, List<Instance>>,
                   val ownedAmisByRegion: Map<String, List<Image>>,
                   val execAmisByRegion: Map<String, List<Image>>,
                   val otherAmisByRegion: Map<String, List<Image>>,
                   val securityGroupsByRegion: Map<String, List<SecurityGroup>>,
                   val elasticIpsByRegion: Map<String, List<Address>>,
                   val keyPairsByRegion: Map<String, List<KeyPairInfo>>,
                   val instanceProfileAssociationsByRegion: Map<String, List<IamInstanceProfileAssociation>>,
                   val clusterGatewaysByRegion: Map<String, List<CustomerGateway>>,
                   val egressOnlyInternetGatewaysByRegion: Map<String, List<EgressOnlyInternetGateway>>,
                   val internetGatewaysByRegion: Map<String, List<InternetGateway>>,
                   val natGatewaysByRegion: Map<String, List<NatGateway>>,
                   val networkAclsByRegion: Map<String, List<NetworkAcl>>,
                   val networkInterfacesByRegion: Map<String, List<NetworkInterface>>,
                   val routeTablesByRegion: Map<String, List<RouteTable>>,
                   val subnetsByRegion: Map<String, List<Subnet>>,
                   val vpcsByRegion: Map<String, List<Vpc>>,
                   val vpcPeeringConnectionsByRegion: Map<String, List<VpcPeeringConnection>>,
                   val vpnGatewaysByRegion: Map<String, List<VpnGateway>>,
                   val vpnConnectionsByRegion: Map<String, List<VpnConnection>>)

data class IamData(val users: List<User>,
                   val groups: List<Group>,
                   val policies: List<Policy>,
                   val roles: List<Role>,
                   val instanceProfiles: List<InstanceProfile>,
                   val virtMfaDevices: List<VirtualMFADevice>,
                   val userGroupAssignmentsByUserArn: Map<String, List<Group>>,
                   val userAccessKeysByUserArn: Map<String, List<AccessKeyMetadata>>,
                   val userAttachedPoliciesByUserArn: Map<String, List<AttachedPolicy>>,
                   val userEmbeddedPolicyNamesByUserArn: Map<String, List<String>>,
                   val userMfaDevicesByUserArn: Map<String, List<MFADevice>>,
                   val userVirtualMfaDevicesByUserArn: Map<String, VirtualMFADevice>,
                   val groupAttachedPoliciesByGroupArn: Map<String, List<AttachedPolicy>>,
                   val groupEmbeddedPolicyNamesByGroupArn: Map<String, List<String>>,
                   val roleAttachedPoliciesByRoleArn: Map<String, List<AttachedPolicy>>,
                   val roleEmbeddedPolicyNamesByRoleArn: Map<String, List<String>>,
                   val roleAssociatedInstanceProfilesByRoleArn: Map<String, List<InstanceProfile>>)

data class RdsData(val dbCertificates: List<Certificate>,
                   val dbClusters: List<DBCluster>,
                   val dbInstances: List<DBInstance>,
                   val dbSecurityGroups: List<DBSecurityGroup>,
                   val dbSubnetGroups: List<DBSubnetGroup>)

fun main(args: Array<String>) {
    println("AWS Crawler")

    val profileName = if (args[0] == "instance") null else args[0]
    val parallelism = args[1].toInt()
    val awsData = Ec2Crawl(AwsCrawlCredentials(profileName = profileName), parallelism).fetchAllData()

    println()
    println("done.")
}

fun printEc2Data(data: Ec2Data) {
    println()
    println("============[ EC2 RESULTS ]=============")
    println()

    println("Account attributes:")
    data.accountAttributes.forEach {
        println("  ${it.attributeName} = ${it.attributeValues.map { it.attributeValue }.joinToString()}")
    }

    println()
    println("Regions:")
    data.regions.forEach {
        println("  $it")
    }

    println()
    println("Availability zones:")
    data.availabilityZonesByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.regionName} :: ${it.zoneName}")
        }
    }

    println()
    println("EC2 Instances:")
    data.instancesByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.tags.nameTag()} :: ${it.instanceId} :: ${it.imageId} :: ${it.keyName} :: ${it.securityGroups.map { it.groupName }.joinToString()}")
        }
    }

    println()
    println("EC2 Instance Profiles Assoc:")
    data.instanceProfileAssociationsByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.instanceId} :: ${it.iamInstanceProfile.id} :: ${it.iamInstanceProfile.arn}")
        }
    }

    println()
    println("EC2 KeyPairs:")
    data.keyPairsByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.keyName} :: ${it.keyFingerprint}")
        }
    }

    println()
    println("Security Groups:")
    data.securityGroupsByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.groupName} :: ${it.groupId} :: ${it.vpcId}")
        }
    }

    println()
    println("Elastic IPs:")
    data.elasticIpsByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.publicIp} :: ${it.privateIpAddress} :: ${it.instanceId}")
        }
    }

    println()
    println("VPCs:")
    data.vpcsByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.tags.nameTag()} :: ${it.vpcId}")
        }
    }

    println()
    println("VPC Peering:")
    data.vpcPeeringConnectionsByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.tags.nameTag()} :: ${it.vpcPeeringConnectionId} :: ${it.accepterVpcInfo.vpcId} <- ${it.requesterVpcInfo.vpcId}")
        }
    }

    println()
    println("EC2 Subnets:")
    data.subnetsByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.tags.nameTag()} :: ${it.vpcId} :: ${it.subnetId} :: ${it.cidrBlock}")
        }
    }

    println()
    println("EC2 Owned AMI Images:")
    data.ownedAmisByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.name ?: it.tags.nameTag()} :: ${it.imageId} :: ${it.architecture} :: ${it.imageType} :: ${it.kernelId} :: ${it.description}")
        }
    }

    println()
    println("EC2 Execute Privileged AMI Images:")
    data.execAmisByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.name ?: it.tags.nameTag()} :: ${it.imageId} :: ${it.architecture} :: ${it.imageType} :: ${it.kernelId} :: ${it.description}")
        }
    }

    println()
    println("EC2 Other Referenced AMI Images:")
    data.otherAmisByRegion.forEach {
        println("  ${it.key}:")
        it.value.forEach {
            println("    ${it.name ?: it.tags.nameTag()} :: ${it.imageId} :: ${it.architecture} :: ${it.imageType} :: ${it.kernelId} :: ${it.description}")
        }
    }
}

fun printIamData(data: IamData) {
    println()
    println("============[ IAM RESULTS ]=============")
    println()

    println("IAM User List:")
    data.users.forEach {
        println("  ${it.userName} :: ${it.userId} :: ${it.arn} :: ")
        println("       groups:          ${data.userGroupAssignmentsByUserArn.get(it.arn)?.map { it.groupName }?.joinToString() ?: "n/a"}")
        println("       keys:            ${data.userAccessKeysByUserArn.get(it.arn)?.map { it.accessKeyId }?.joinToString() ?: "n/a"}")
        println("       VirtMFA devices: ${data.userVirtualMfaDevicesByUserArn.get(it.arn)?.let { it.serialNumber } ?: "n/a"}")
        println("       MFA devices:     ${data.userMfaDevicesByUserArn.get(it.arn)?.map { it.serialNumber }?.joinToString() ?: "n/a"}")
        println("       policies:        ${data.userEmbeddedPolicyNamesByUserArn.get(it.arn)?.joinToString() ?: "n/a"}")
        println("       ext policies:    ${data.userAttachedPoliciesByUserArn.get(it.arn)?.map { it.policyName }?.joinToString() ?: "n/a"}")
    }

    println()
    println("IAM Group List:")
    data.groups.forEach {
        println("  ${it.groupName} :: ${it.groupId} :: ${it.arn}")
        println("       policies:      ${data.groupEmbeddedPolicyNamesByGroupArn.get(it.arn)?.joinToString() ?: "n/a"}")
        println("       ext policies:  ${data.groupAttachedPoliciesByGroupArn.get(it.arn)?.map { it.policyName }?.joinToString() ?: "n/a"}")
    }

    println()
    println("IAM Role List:")
    data.roles.forEach {
        println("  ${it.roleName} :: ${it.roleId} :: ${it.arn}")
        println("       policies:      ${data.roleEmbeddedPolicyNamesByRoleArn.get(it.arn)?.joinToString() ?: "n/a"}")
        println("       ext policies:  ${data.roleAttachedPoliciesByRoleArn.get(it.arn)?.map { it.policyName }?.joinToString() ?: "n/a"}")
        println("       inst profiles: ${data.roleAssociatedInstanceProfilesByRoleArn.get(it.arn)?.map { it.instanceProfileName }?.joinToString() ?: "n/a"}")
    }

    println()
    println("IAM Policy List:")
    data.policies.forEach {
        println("  ${it.policyName} :: ${it.policyId} :: ${it.arn}")
    }

    println()
    println("IAM Instance Profiles List:")
    data.instanceProfiles.forEach {
        println("  ${it.instanceProfileName} :: ${it.instanceProfileId} :: ${it.arn}")
    }
}

fun printRdsData(data: RdsData) {
    println()
    println("============[ RDS RESULTS ]=============")
    println()

    println()
    println("RDS Certificates:")
    data.dbCertificates.forEach {
        println("    ${it.certificateIdentifier} :: ${it.certificateType} :: ${it.certificateArn}")
    }

    println()
    println("RDS Clusters:")
    data.dbClusters.forEach {
        println("    ${it.databaseName} :: ${it.dbClusterIdentifier} :: ${it.engine} :: ${it.engineVersion} :: ${it.dbClusterArn}")
    }

    println()
    println("RDS Instances:")
    data.dbInstances.forEach {
        println("    ${it.dbName} :: ${it.dbInstanceIdentifier} :: ${it.dbInstanceClass} :: ${it.dbInstanceArn}")
    }

    println()
    println("RDS Security Groups:")
    data.dbSecurityGroups.forEach {
        println("    ${it.vpcId} :: ${it.dbSecurityGroupName} :: ${it.dbSecurityGroupArn}")
    }

    println()
    println("RDS Subnet Groups:")
    data.dbSubnetGroups.forEach {
        println("    ${it.vpcId} :: ${it.dbSubnetGroupName} :: ${it.dbSubnetGroupArn} :: ${it.subnets.map { it.subnetIdentifier }.joinToString()}")
    }
}

fun List<Tag>.nameTag() = firstOrNull { it.key.equals("Name", ignoreCase = true) }?.value ?: "unknown"
