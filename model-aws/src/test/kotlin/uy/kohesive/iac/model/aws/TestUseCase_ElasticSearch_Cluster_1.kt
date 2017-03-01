package uy.kohesive.iac.model.aws

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.cloudformation.CasePreservingNamingStrategy
import uy.kohesive.iac.model.aws.cloudformation.TemplateBuilder
import uy.kohesive.iac.model.aws.helpers.*
import uy.kohesive.iac.model.aws.proxy.createLiteralReference

// TODO: we should have instance lists auto generated and kept up to date with an automatic build system, so a library
//       that is generated from the region => service => pricing API
// Long lists and mappings that are known values should never need to be made again like the following, we would instead
// just have some other defined types which represent InstanceList for example (and maybe a way to easily filter by architecture
// and region availability).
//
// possible places to generate from:
//    http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/instance-types.html
// note we might want pre-built filters by "can be in VPC", "has enhanced networking"
//
// http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/virtualization_types.html
//
// maybe we can also have filters by CPU and memory, so you can say "instances types with 4 or more cores and between 8 and 32G of memory"
//    https://aws.amazon.com/ec2/instance-types/
// this link has a good matrix at the bottom (storage type, network perf, clock speed, EBS optimized, enhanced network, ...)
//
// instance Linux architecture matrix:
//    https://aws.amazon.com/amazon-linux-ami/instance-type-matrix/
//
// only in a VPC:
//     http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-vpc.html#vpc-only-instance-types
// enhanced networking:
//     http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/enhanced-networking.html
// hardware accelerated:
//     http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/accelerated-computing-instances.html
//
// TODO: anything different for windows type instances?
//
// NOTE:  pricing API might be able to infer some of this
//     https://aws.amazon.com/blogs/aws/new-aws-price-list-api/
// this shows a lambda that will pull and create a pricing JSON on any pricing model change:
//     https://blog.rackspace.com/experimenting-aws-price-list-api
//
// it appears Amazon has .js files which contain information in them, and are probably generated, this code accesses those:
//     https://github.com/erans/ec2instancespricing/blob/master/ec2instancespricing/ec2instancespricing.py
// and also has a good list of OS types and other static type lists.
// alternative fork of same script that does more services:
//     https://github.com/ilia-semenov/awspricingfull/blob/master/awspricingfull.py

fun main(args: Array<String>) {
    TestUseCase_ElasticSearch_Cluster_1().`test making an Elasticsearch cluster using the SDK`()
}

class TestUseCase_ElasticSearch_Cluster_1 {
    fun `test making an Elasticsearch cluster using the SDK`() {

        // ===[ VARIABLES ]=============================================================================================

        val keyNameParameter = ParameterizedValue("KeyName",
            type = ParameterizedValueTypes.EC2KeyPairKeyName, // TODO: shouldn't it be String?
            constraintDescription = "KeyPair name from 1 to 255 ASCII characters."
        )

        val clusterSize = ParameterizedValue("ClusterSize",
            type         = ParameterizedValueTypes.Number,
            description  = "The number of Elasticsearch instances to launch in the Auto Scaling group",
            defaultValue = "3"
        )

        val instanceTypeParam = ParameterizedValue("InstanceType", type = ParameterizedValueTypes.String,
            defaultValue     = "m3.large",
            description      = "EC2 Instance type.",
            errorDescription = "Must be a valid Amazon EC2 instance type.",
            allowedValues    = listOf(
                "t1.micro",
                "m1.small",
                "m1.medium",
                "m1.large",
                "m1.xlarge",
                "m3.medium",
                "m3.large",
                "m3.xlarge",
                "m3.2xlarge",
                "c1.medium",
                "c1.xlarge",
                "c3.large",
                "c3.xlarge",
                "c3.2xlarge",
                "c3.4xlarge",
                "c3.8xlarge",
                "cc2.8xlarge",
                "m2.xlarge",
                "m2.2xlarge",
                "m2.4xlarge",
                "r3.large",
                "r3.xlarge",
                "r3.2xlarge",
                "r3.4xlarge",
                "r3.8xlarge",
                "cr1.8xlarge",
                "hi1.4xlarge",
                "hs1.8xlarge",
                "i2.xlarge",
                "i2.2xlarge",
                "i2.4xlarge",
                "i2.8xlarge"
            )
        )

        // TODO:  IP CIDR should be built-in type (our own, doesn't exist in Cloud Formation)
        val sshLocation = ParameterizedValue("SSHLocation",
            defaultValue     = "0.0.0.0/0",
            description      = "IP CIDR range for allowing SSH access to the instances",
            errorDescription = "Must be a valid IP CIDR range of the form x.x.x.x/x.",
            type             = ParameterizedValueTypes.String,
            allowedLength    = 8..18,
            allowedPattern   = """(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})/(\d{1,2})""".toRegex()
        )

        val elasticsearchVersion = ParameterizedValue("ElasticsearchVersion",
            defaultValue     = "5.2.1",
            description      = "Elasticsearch version number",
            errorDescription = "Must be a supported version number.",
            type             = ParameterizedValueTypes.String,
            allowedValues    = listOf("5.2.1")
        )

        // ===[ MAPPINGS ]==============================================================================================

        val awsInstantType2Arch = MappedValues("AWSInstanceType2Arch", mapOf(
            "t1.micro"    to mapOf("Arch" to "64"),
            "m1.small"    to mapOf("Arch" to "64"),
            "m1.medium"   to mapOf("Arch" to "64"),
            "m1.large"    to mapOf("Arch" to "64"),
            "m1.xlarge"   to mapOf("Arch" to "64"),
            "m3.medium"   to mapOf("Arch" to "64"),
            "m3.large"    to mapOf("Arch" to "64"),
            "m3.xlarge"   to mapOf("Arch" to "64"),
            "m3.2xlarge"  to mapOf("Arch" to "64"),
            "c1.medium"   to mapOf("Arch" to "64"),
            "c1.xlarge"   to mapOf("Arch" to "64"),
            "c3.large"    to mapOf("Arch" to "64"),
            "c3.xlarge"   to mapOf("Arch" to "64"),
            "c3.2xlarge"  to mapOf("Arch" to "64"),
            "c3.4xlarge"  to mapOf("Arch" to "64"),
            "c3.8xlarge"  to mapOf("Arch" to "64"),
            "cc2.8xlarge" to mapOf("Arch" to "64HVM"),
            "m2.xlarge"   to mapOf("Arch" to "64"),
            "m2.2xlarge"  to mapOf("Arch" to "64"),
            "m2.4xlarge"  to mapOf("Arch" to "64"),
            "r3.large"    to mapOf("Arch" to "64HVM"),
            "r3.xlarge"   to mapOf("Arch" to "64HVM"),
            "r3.2xlarge"  to mapOf("Arch" to "64HVM"),
            "r3.4xlarge"  to mapOf("Arch" to "64HVM"),
            "r3.8xlarge"  to mapOf("Arch" to "64HVM"),
            "cr1.8xlarge" to mapOf("Arch" to "64HVM"),
            "hi1.4xlarge" to mapOf("Arch" to "64HVM"),
            "hs1.8xlarge" to mapOf("Arch" to "64HVM"),
            "i2.xlarge"   to mapOf("Arch" to "64HVM"),
            "i2.2xlarge"  to mapOf("Arch" to "64HVM"),
            "i2.4xlarge"  to mapOf("Arch" to "64HVM"),
            "i2.8xlarge"  to mapOf("Arch" to "64HVM")
        ))

        val awsRegionArchi2Ami = MappedValues("AWSRegionArch2AMI", mapOf(
            "us-east-1" to mapOf("64" to "ami-fb8e9292", "64HVM" to "ami-978d91fe"),
            "us-west-1" to mapOf("64" to "ami-7aba833f", "64HVM" to "ami-5aba831f"),
            "us-west-2" to mapOf("64" to "ami-043a5034", "64HVM" to "ami-383a5008"),
            "eu-west-1" to mapOf("64" to "ami-2918e35e", "64HVM" to "ami-4b18e33c"),
            "sa-east-1" to mapOf("64" to "ami-215dff3c", "64HVM" to "ami-635dff7e"),
            "ap-southeast-1" to mapOf("64" to "ami-b40d5ee6", "64HVM" to "ami-860d5ed4"),
            "ap-southeast-2" to mapOf("64" to "ami-3b4bd301", "64HVM" to "ami-cf4ad2f5"),
            "ap-northeast-1" to mapOf("64" to "ami-c9562fc8", "64HVM" to "ami-bb562fba")
        ))

        // ===[ BUILDING ]==============================================================================================

        val context = IacContext("test", "es-cluster-91992881DX") {
            addVariables(keyNameParameter, instanceTypeParam, sshLocation, clusterSize, elasticsearchVersion)
            addMappings(awsInstantType2Arch, awsRegionArchi2Ami)

            val esInstanceProfile = withIamContext {
                val clusterDiscoveryRole = createRole {
                    roleName = "ElasticsearchDiscoveryRole"
                    assumeRoleFromPrincipal = AssumeRolePrincipals.EC2
                }

                val allowDiscoveryPolicy = createPolicy {
                    policyName = "ElasticsearchAllowEc2DescribeInstances"
                    policyFromStatement = CustomPolicyStatement(PolicyEffect.Allow, "ec2:DescribeInstances", "*")
                }

                attachIamRolePolicy(clusterDiscoveryRole, allowDiscoveryPolicy)

                val esInstanceProfile = createInstanceProfile {
                    instanceProfileName = "ElasticsearchInstanceProfile"
                }

                addRoleToInstanceProfile(clusterDiscoveryRole, esInstanceProfile)

                esInstanceProfile
            }

            withAutoScalingContext {
                val launchConfiguration = createLaunchConfiguration {
                    launchConfigurationName = "ElasticsearchServer"
                    iamInstanceProfile = esInstanceProfile.arn

                    // TODO: metadata?

                    imageId = awsRegionArchi2Ami.asRef(
                        keyVariable = createLiteralReference("AWS::Region"),
                        valueVariable = awsInstantType2Arch.asRef(
                            keyVariable = instanceTypeParam.asStringRef(),
                            valueVariable = "Arch"
                        )
                    )
                    instanceType = instanceTypeParam.asStringRef()
                    keyName = keyNameParameter.asStringRef()

                    // TODO: security group
                    // TODO: user data
                }

                createAutoScalingGroup {
                    // TODO: availability zones set as `Fn::GetAZs`, have to decide later if we want a similar function (to match region)
                    //       since likely we don't really want to just pass these through as function calls specific to cloud formation
                    autoScalingGroupName = "ElasticsearchServerGroup"
                    launchConfigurationName = launchConfiguration.launchConfigurationName
                    minSize = 1
                    maxSize = 12
                    desiredCapacity = clusterSize.asIntRef()
                    withTags(createTag("type", "elasticsearch").withPropagateAtLaunch(true))
                }
            }

            withEc2Context {
                // ec2Client.runInstances(RunInstancesRequest())
            }
        }


        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .writerWithDefaultPrettyPrinter()

        val cfTemplate = TemplateBuilder(context, description = "ElasticSearch Cluster.").build()

        println(JSON.writeValueAsString(cfTemplate))
    }
}

