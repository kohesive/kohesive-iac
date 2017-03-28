package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object WorkSpaces {

    @CloudFormationType("AWS::WorkSpaces::Workspace")
    data class Workspace(
        val BundleId: String,
        val DirectoryId: String,
        val UserName: String,
        val RootVolumeEncryptionEnabled: String? = null,
        val UserVolumeEncryptionEnabled: String? = null,
        val VolumeEncryptionKey: String? = null
    ) : ResourceProperties 


}