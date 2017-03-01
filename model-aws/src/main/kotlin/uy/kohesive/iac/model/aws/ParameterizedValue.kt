package uy.kohesive.iac.model.aws

class ParameterizedValue(val name: String,
                         val type: ParameterizedValueTypes,
                        val defaultValue: String? = null,
                        val allowedLength: IntRange? = null,
                        val allowedNumericValues: LongRange? = null,
                        val allowedPattern: Regex? = null,
                        val allowedValues: List<String> = emptyList(),
                        val noEcho: Boolean = false,
                        val description: String? = null,
                        val errorDescription: String? = null,
                        val constraintDescription: String? = null) {
    // TODO: factory methods that limit parameter set by the type so it adds less noise
}

enum class ParameterizedValueTypes(val cloudFormationName: kotlin.String, val defaultDescription: kotlin.String? = null, val defaultErrorDescription: kotlin.String? = null) {
    String("String"),
    Number("Number"),
    NumberList("List<Number>"),
    CommaDelimitedList("CommaDelimitedList"),
    // TODO: generate these from http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/parameters-section-structure.html
    //    AWS::Something::Foo::Bar  =>   SomethingFooBar
    //    List<AWS::Something::Foo::Bar> =>   SomethingFooBarList
    // and in description rename the word "array" to "list", and delete suffic part starting with "Note that"
    EC2AvailabilityZoneName("AWS::EC2::AvailabilityZone::Name", "An Availability Zone, such as us-west-2a"),
    EC2ImageId("AWS::EC2::Image::Id", "An Amazon EC2 image ID, such as ami-ff527ecf."),
    EC2InstanceId("AWS::EC2::Instance::Id", "An Amazon EC2 instance ID, such as i-1e731a32."),
    EC2KeyPairKeyName("AWS::EC2::KeyPair::KeyName", "An Amazon EC2 key pair name."),
    EC2SecurityGroupGroupName("AWS::EC2::SecurityGroup::GroupName", "An EC2-Classic or default VPC security group name, such as my-sg-abc."),
    EC2SecurityGroupId("AWS::EC2::SecurityGroup::Id", "A security group ID, such as sg-a123fd85."),
    EC2SubnetId("AWS::EC2::Subnet::Id", "A subnet ID, such as subnet-123a351e."),
    EC2VolumeId("AWS::EC2::Volume::Id", "An Amazon EBS volume ID, such as vol-3cdd3f56."),
    EC2VPCId("AWS::EC2::VPC::Id", "A VPC ID, such as vpc-a123baa3."),
    Route53HostedZoneId("AWS::Route53::HostedZone::Id", "An Amazon Route 53 hosted zone ID, such as Z23YXV4OVPL04A."),
    EC2AvailabilityZoneNameList("List<AWS::EC2::AvailabilityZone::Name>", "An list of Availability Zones for a region, such as us-west-2a, us-west-2b."),
    EC2ImageIdList("List<AWS::EC2::Image::Id>", "An list of Amazon EC2 image IDs, such as ami-ff527ecf, ami-e7527ed7. "),
    EC2InstanceIdList("List<AWS::EC2::Instance::Id>", "An list of Amazon EC2 instance IDs, such as i-1e731a32, i-1e731a34."),
    EC2SecurityGroupGroupNameList("List<AWS::EC2::SecurityGroup::GroupName>", "An list of EC2-Classic or default VPC security group names, such as my-sg-abc, my-sg-def."),
    EC2SecurityGroupIdList("List<AWS::EC2::SecurityGroup::Id>", "An list of security group IDs, such as sg-a123fd85, sg-b456fd85."),
    EC2SubnetIdList("List<AWS::EC2::Subnet::Id>", "An list of subnet IDs, such as subnet-123a351e, subnet-456b351e."),
    EC2VolumeIdList("List<AWS::EC2::Volume::Id>", "An list of Amazon EBS volume IDs, such as vol-3cdd3f56, vol-4cdd3f56."),
    EC2VPCIdList("List<AWS::EC2::VPC::Id>", "An list of VPC IDs, such as vpc-a123baa3, vpc-b456baa3."),
    Route53HostedZoneIdList("List<AWS::Route53::HostedZone::Id>", "An list of Amazon Route 53 hosted zone IDs, such as Z23YXV4OVPL04A, Z23YXV4OVPL04B.")
}