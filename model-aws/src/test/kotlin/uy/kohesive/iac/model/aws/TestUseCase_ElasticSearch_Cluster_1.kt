package uy.kohesive.iac.model.aws

import com.amazonaws.services.ec2.model.IpPermission
import com.amazonaws.services.ec2.model.IpRange
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.cloudformation.TemplateBuilder
import uy.kohesive.iac.model.aws.helpers.*
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

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

        val keyNameParam = ParameterizedValue.newTyped("KeyName", ParameterizedValueTypes.EC2KeyPairKeyName,
            constraintDescription = "KeyPair name from 1 to 255 ASCII characters."
        )

        val clusterSizeParam = ParameterizedValue.newInt("ClusterSize",
            description  = "The number of Elasticsearch instances to launch in the Auto Scaling group",
            defaultValue = 3
        )

        val instanceTypeParam = ParameterizedValue.newTyped("InstanceType", ParameterizedValueTypes.String,
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
        val sshLocationParam = ParameterizedValue.newString("SSHLocation",
            defaultValue     = "0.0.0.0/0",
            description      = "IP CIDR range for allowing SSH access to the instances",
            errorDescription = "Must be a valid IP CIDR range of the form x.x.x.x/x.",
            allowedLength    = 8..18,
            allowedPattern   = """(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})/(\d{1,2})""".toRegex()
        )

        val elasticsearchVersionParam = ParameterizedValue.newString("ElasticsearchVersion",
            defaultValue     = "5.2.1",
            description      = "Elasticsearch version number",
            errorDescription = "Must be a supported version number.",
            allowedValues    = listOf("5.2.1")
        )

        // ===[ MAPPINGS ]==============================================================================================

        val awsInstantType2ArchMap = MappedValues("AWSInstanceType2Arch", mapOf(
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

        val awsRegionArchi2AmiMap = MappedValues("AWSRegionArch2AMI", mapOf(
            "us-east-1" to mapOf("64" to "ami-fb8e9292", "64HVM" to "ami-978d91fe"),
            "us-west-1" to mapOf("64" to "ami-7aba833f", "64HVM" to "ami-5aba831f"),
            "us-west-2" to mapOf("64" to "ami-043a5034", "64HVM" to "ami-383a5008"),
            "eu-west-1" to mapOf("64" to "ami-2918e35e", "64HVM" to "ami-4b18e33c"),
            "sa-east-1" to mapOf("64" to "ami-215dff3c", "64HVM" to "ami-635dff7e"),
            "ap-southeast-1" to mapOf("64" to "ami-b40d5ee6", "64HVM" to "ami-860d5ed4"),
            "ap-southeast-2" to mapOf("64" to "ami-3b4bd301", "64HVM" to "ami-cf4ad2f5"),
            "ap-northeast-1" to mapOf("64" to "ami-c9562fc8", "64HVM" to "ami-bb562fba")
        ))

        val elasticsearchVersion2ServiceWrapperHashMap = MappedValues("ElasticsearchVersion2ServiceWrapperHash", mapOf(
            "1.4.2" to mapOf("Hash" to "4943d5a")
        ))

        val elasticsearchVersion2AWSCloudPluginVersion = MappedValues("ElasticsearchVersion2AWSCloudPluginVersion", mapOf(
            "1.4.2" to mapOf("Ver" to "2.4.1")
        ))

        // ===[ BUILDING ]==============================================================================================

        val context = IacContext("test", "es-cluster-91992881DX") {
            addVariables(keyNameParam, instanceTypeParam, sshLocationParam, clusterSizeParam, elasticsearchVersionParam)
            addMappings(awsInstantType2ArchMap, awsRegionArchi2AmiMap, elasticsearchVersion2ServiceWrapperHashMap, elasticsearchVersion2AWSCloudPluginVersion)

            val esInstanceProfile = withIamContext {
                val clusterDiscoveryRole = createRole("ElasticsearchDiscoveryRole") {
                    assumeRoleFromPrincipal = AssumeRolePrincipals.EC2
                }

                val allowDiscoveryPolicy = createPolicy("ElasticsearchAllowEc2DescribeInstances") {
                    policyFromStatement = CustomPolicyStatement(PolicyEffect.Allow, "ec2:DescribeInstances", "*")
                }

                attachIamRolePolicy(clusterDiscoveryRole, allowDiscoveryPolicy)

                val esInstanceProfile = createInstanceProfile("ElasticsearchInstanceProfile") {
                    path = "/"
                }

                addRoleToInstanceProfile(clusterDiscoveryRole, esInstanceProfile)
                esInstanceProfile
            }

            val securityGroupId = withEc2Context {
                val groupId = createSecurityGroup("ElasticsearchSecurityGroup") {
                    description = "Enable Elasticsearch access"
                }

                authorizeSecurityGroupIngress("ElasticsearchSecurityGroup") {
                    withIpPermissions(
                        IpPermission()
                            .withIpProtocol("tcp")
                            .withFromPort(9200)
                            .withToPort(9200)
                            .withIpv4Ranges(IpRange().withCidrIp("0.0.0.0/0")),
                        IpPermission()
                            .withIpProtocol("tcp")
                            .withFromPort(9300)
                            .withToPort(9300)
                            .withIpv4Ranges(IpRange().withCidrIp("0.0.0.0/0")),
                        IpPermission()
                            .withIpProtocol("tcp")
                            .withFromPort(22)
                            .withToPort(22)
                            .withIpv4Ranges(IpRange().withCidrIp(sshLocationParam.value))
                    )
                }

                return@withEc2Context groupId
            }

            withAutoScalingContext {
                val launchConfiguration = createLaunchConfiguration("ElasticsearchServer") {
                    val awsCloudPluginVersion = elasticsearchVersion2AWSCloudPluginVersion.asLookup(
                        keyVariable   = elasticsearchVersionParam.value,
                        valueVariable = "Ver"
                    )
                    val esServiceWrapperHash = elasticsearchVersion2ServiceWrapperHashMap.asLookup(
                        keyVariable   = elasticsearchVersionParam.value,
                        valueVariable = "Hash"
                    )

                    iamInstanceProfile = esInstanceProfile.arn

                    // TODO: metadata?
                    // TODO: this is not very friendly vvv
                    //     maybe some X.path(abc).path(yyz) or json path looking thing
                    //     in the case below would be:
                    //        awsRegionArchi2AmiMap.path(ImplicitValues.Region.value).path(awsInstantType2ArchMap.path(instanceTypeParam.value).path("Arch").value).value
                    //     or
                    //        awsRegionArchi2AmiMap.path(ImplicitValues.Region.value)[awsInstantType2ArchMap.path(instanceTypeParam.value)["Arch"]]
                    //     or
                    //        awsRegionArchi2AmiMap.path(ImplicitValues.Region.value).get(awsInstantType2ArchMap.path(instanceTypeParam.value).get("Arch"))
                    //     or
                    //        awsRegionArchi2AmiMap.get(ImplicitValues.Region.value).get(awsInstantType2ArchMap.get(instanceTypeParam.value).get("Arch"))
                    //     or because get() is also []...
                    //        awsRegionArchi2AmiMap[ImplicitValues.Region.value][awsInstantType2ArchMap[instanceTypeParam.value]["Arch"]]
                    //
                    imageId = awsRegionArchi2AmiMap.asLookup(
                        keyVariable   = ImplicitValues.Region.value,
                        valueVariable = awsInstantType2ArchMap.asLookup(
                            keyVariable   = instanceTypeParam.value,
                            valueVariable = "Arch"
                        )
                    )
                    instanceType = instanceTypeParam.value
                    keyName      = keyNameParam.value
                    userData     = """
                        #!/bin/bash
                        yum update -y aws-cfn-bootstrap
                        # Helper function
                        function error_exit
                        {
                          #/opt/aws/bin/cfn-signal -e 1 -r "$1" 'TODO:WaitHandle'
                          exit 1
                        }
                        # Install application
                        #/opt/aws/bin/cfn-init -s ${ImplicitValues.StackId.value} -r ElasticsearchServer --region ${ImplicitValues.Region.value}
                        #get and unzip elasticsearch
                        wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-${elasticsearchVersionParam.value}.zip || error_exit "Failed to retrieve elasticsearch archive"
                        unzip elasticsearch-${elasticsearchVersionParam.value}.zip -d /usr/local/elasticsearch
                        #install aws plugin
                        cd /usr/local/elasticsearch/elasticsearch-${elasticsearchVersionParam.value}
                        res=$(bin/plugin -install org.elasticsearch/elasticsearch-cloud-aws/$awsCloudPluginVersion)
                        if [ "$?" -ne "0" ]; then
                           error_exit "Failed to install aws plugin: $\{res}"
                        fi
                        # Install elasticsearch config.yml
                        /opt/aws/bin/cfn-init -s ${ImplicitValues.StackId.value} -r ElasticsearchServer --region ${ImplicitValues.Region.value} || error_exit "failed to run cfn-init"
                        #install elasticsearch servicewrapper daemon
                        cd ~
                        wget https://github.com/elasticsearch/elasticsearch-servicewrapper/zipball/$esServiceWrapperHash
                        unzip $esServiceWrapperHash
                        mv elasticsearch-elasticsearch-servicewrapper-$esServiceWrapperHash/service/ /usr/local/elasticsearch/elasticsearch-${elasticsearchVersionParam.value}/bin/
                        cd /usr/local/elasticsearch/elasticsearch-${elasticsearchVersionParam.value}
                        sed -i 's#set.default.ES_HOME=.*#set.default.ES_HOME='$\PWD'#g' bin/service/elasticsearch.conf
                        #changing default heap size for smaller instances
                        sed -i 's#set.default.ES_HEAP_SIZE=.*#set.default.ES_HEAP_SIZE=512#g' bin/service/elasticsearch.conf
                        bin/service/elasticsearch64 install || error_exit "Failed install elasticsearch daemon"
                        res=$(bin/service/elasticsearch64 start)
                        if [ "$?" -ne "0" ]; then
                           error_exit "Failed to start elasticsearch servicewrapper: $\{res}"
                        fi
                        # All is well so signal success
                        #/opt/aws/bin/cfn-signal -e $? 'TODO:WaitHandle'
                    """.trimIndent()
                    withSecurityGroups(securityGroupId)
                }

                createAutoScalingGroup("ElasticsearchServerGroup") {
                    // TODO: availability zones set as `Fn::GetAZs`, have to decide later if we want a similar function (to match region)
                    //       since likely we don't really want to just pass these through as function calls specific to cloud formation
                    launchConfigurationName = launchConfiguration.launchConfigurationName
                    minSize = 1
                    maxSize = 12
                    desiredCapacity = clusterSizeParam.value
                    withTags(createTag("type", "elasticsearch").withPropagateAtLaunch(true))
                }
            }
        }

        val JsonWriter = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .writerWithDefaultPrettyPrinter()

        val cfTemplate = TemplateBuilder(context, description = "ElasticSearch Cluster.").build()

        println(JsonWriter.writeValueAsString(cfTemplate))
    }
}

