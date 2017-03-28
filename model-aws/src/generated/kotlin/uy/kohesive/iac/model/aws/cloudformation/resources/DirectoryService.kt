package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object DirectoryService {

    @CloudFormationType("AWS::DirectoryService::MicrosoftAD")
    data class MicrosoftAD(
        val CreateAlias: String? = null,
        val EnableSso: String? = null,
        val Name: String,
        val Password: String,
        val ShortName: String? = null,
        val VpcSettings: MicrosoftAD.VpcSettingProperty
    ) : ResourceProperties {

        data class VpcSettingProperty(
            val SubnetIds: List<String>,
            val VpcId: String
        ) 

    }

    @CloudFormationType("AWS::DirectoryService::SimpleAD")
    data class SimpleAD(
        val CreateAlias: String? = null,
        val Description: String? = null,
        val EnableSso: String? = null,
        val Name: String,
        val Password: String,
        val ShortName: String? = null,
        val Size: String,
        val VpcSettings: SimpleAD.VpcSettingProperty
    ) : ResourceProperties {

        data class VpcSettingProperty(
            val SubnetIds: List<String>,
            val VpcId: String
        ) 

    }


}