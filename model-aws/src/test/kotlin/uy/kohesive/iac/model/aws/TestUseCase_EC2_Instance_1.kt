package uy.kohesive.iac.model.aws

import com.amazonaws.services.ec2.model.IpPermission
import com.amazonaws.services.ec2.model.IpRange
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import junit.framework.TestCase
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationContext
import uy.kohesive.iac.model.aws.cloudformation.TemplateBuilder
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class TestUseCase_EC2_Instance_1 : TestCase() {

    fun `test launching EC2 instance using the SDK`() {
        val keyNameParam = ParameterizedValue.newTyped("KeyName", ParameterizedValueTypes.EC2KeyPairKeyName,
            constraintDescription = "KeyPair name from 1 to 255 ASCII characters.",
            description           = "Name of an existing EC2 KeyPair to enable SSH access to the instance"
        )

        val instanceTypeParam = ParameterizedValue.newTyped("InstanceType", ParameterizedValueTypes.String,
            defaultValue     = "t2.small",
            description      = "WebServer EC2 instance type",
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

        val sshLocationParam = ParameterizedValue.newString("SSHLocation",
            defaultValue     = "0.0.0.0/0",
            description      = "IP CIDR range for allowing SSH access to the instances",
            errorDescription = "Must be a valid IP CIDR range of the form x.x.x.x/x.",
            allowedLength    = 8..18,
            allowedPattern   = """(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})/(\d{1,2})""".toRegex()
        )

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

        val context = CloudFormationContext("test", "ec2-instance-simple") {
            addVariables(keyNameParam, instanceTypeParam, sshLocationParam)
            addMappings(awsInstantType2ArchMap, awsRegionArchi2AmiMap)

            withEC2Context {
                val securityGroup = createSecurityGroup("InstanceSecurityGroup") {
                    description = "Enable SSH access via port 22"
                }

                authorizeSecurityGroupIngress("InstanceSecurityGroup") {
                    withIpPermissions(
                        IpPermission()
                            .withIpProtocol("tcp")
                            .withFromPort(22)
                            .withToPort(22)
                            .withIpv4Ranges(IpRange().withCidrIp(sshLocationParam.value))
                    )
                }

                val reservation = runInstances("EC2Instance") {
                    instanceType = instanceTypeParam.value
                    keyName      = keyNameParam.value
                    imageId      = awsRegionArchi2AmiMap[ImplicitValues.Region.value][awsInstantType2ArchMap[instanceTypeParam.value]["Arch"]]
                    setSecurityGroups(listOf(securityGroup))
                }

                val instance = reservation.instances.first()

                modifyPlacement(instance.instanceId) {
                    affinity = "host"
                    tenancy  = "host"
                    hostId   = "someHostId"
                }

                addAsOutput("InstanceId", instance.instanceId,      "InstanceId of the newly created EC2 instance")
                addAsOutput("PublicDNS",  instance.publicDnsName,   "Public DNSName of the newly created EC2 instance")
                addAsOutput("PublicIP",   instance.publicIpAddress, "Public IP address of the newly created EC2 instance")
            }
        }

        val JsonWriter = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .writerWithDefaultPrettyPrinter()

        val cfTemplate = TemplateBuilder(context, description = "Create an Amazon EC2 instance running the Amazon Linux AMI.").build()

        println(JsonWriter.writeValueAsString(cfTemplate))
    }

}
