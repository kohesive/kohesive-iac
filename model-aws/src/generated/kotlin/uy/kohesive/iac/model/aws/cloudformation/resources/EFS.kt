package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object EFS {

    @CloudFormationType("AWS::EFS::FileSystem")
    data class FileSystem(
        val FileSystemTags: List<EFS.FileSystem.FileSystemTagProperty>? = null,
        val PerformanceMode: String? = null
    ) : ResourceProperties {

        data class FileSystemTagProperty(
            val Key: String? = null,
            val Value: String? = null
        ) 

    }

    @CloudFormationType("AWS::EFS::MountTarget")
    data class MountTarget(
        val FileSystemId: String,
        val IpAddress: String? = null,
        val SecurityGroups: List<String>,
        val SubnetId: String
    ) : ResourceProperties 


}