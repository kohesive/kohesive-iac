package uy.kohesive.iac.model.aws.template

import junit.framework.TestCase

class ReferenceExploderTest : TestCase() {

    // TODO: make unit tests for those
    val references = listOf(
        "{{kohesive:map:AWSRegionArch2AMI:{{kohesive:ivar:AWS-Region}}:{{kohesive:map:AWSInstanceType2Arch:{{kohesive:var:InstanceType}}:Arch}}}}",
        "Hello, {{kohesive:var:InstanceType}} instance!",
        "{{kohesive:var:KeyName}}",
        "{{kohesive:ref-property:InstanceProfile:ElasticsearchInstanceProfile:Arn}}"
    )

}