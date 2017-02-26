package uy.kohesive.iac.model.aws

import com.amazonaws.services.ec2.model.RunInstancesRequest
import org.junit.Test
import uy.kohesive.iac.model.aws.proxy.IacContext

class TestUseCase_ElasticSearch_Cluster_1 {
    fun `test making an Elasticsearch cluster using the SDK`() {
        val keyNameParameter = ParameterizedValue("KeyName", type = ParameterizedValueTypes.EC2KeyPairKeyName)

        // TODO: we should have instance lists auto generated and kept up to date with an automatic build system, so a library
        //       that is generated from the region => service => pricing API
        // Long lists and mappings that are known values should never need to be made again like the following, we would instead
        // just have some other defined types which represent InstanceList for example (and maybe a way to easily filter by architecture
        // and region availability).

        val instanceType = ParameterizedValue("InstanceType", type = ParameterizedValueTypes.String,
                defaultValue = "m3.large",
                description = "EC2 Instance type.",
                errorDescription = "Must be a valid Amazon EC2 instance type.",
                allowedValues = listOf(
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
        ))

        // TODO:  IP CIDR should be built-in type (our own, doesn't exist in Cloud Formation)
        val sshLocation = ParameterizedValue("SSHLocation",
                defaultValue = "0.0.0.0/0",
                description = "IP CIDR range for allowing SSH access to the instances",
                errorDescription = "Must be a valid IP CIDR range of the form x.x.x.x/x.",
                type = ParameterizedValueTypes.String,
                allowedLength = 8..18,
                allowedPattern = """(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})/(\d{1,2})""".toRegex())

        val elasticsearchVersion = ParameterizedValue("ElasticsearchVersion",
                defaultValue = "5.2.1",
                description = "Elasticsearch version number",
                errorDescription = "Must be a supported version number.",
                type = ParameterizedValueTypes.String,
                allowedValues = listOf("5.2.1"))



        IacContext("test", "es-cluster-91992881DX") {
            addVariables(keyNameParameter, instanceType, sshLocation, elasticsearchVersion)

            // ec2Client.runInstances(RunInstancesRequest())
        }

    }

}