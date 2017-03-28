package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object GameLift {

    @CloudFormationType("AWS::GameLift::Alias")
    data class Alias(
        val Description: String? = null,
        val Name: String,
        val RoutingStrategy: Alias.RoutingStrategyProperty
    ) : ResourceProperties {

        data class RoutingStrategyProperty(
            val FleetId: String? = null,
            val Message: String? = null,
            val Type: String
        ) 

    }

    @CloudFormationType("AWS::GameLift::Build")
    data class Build(
        val Name: String? = null,
        val StorageLocation: Build.StorageLocationProperty? = null,
        val Version: String? = null
    ) : ResourceProperties {

        data class StorageLocationProperty(
            val Bucket: String,
            val Key: String,
            val RoleArn: String
        ) 

    }

    @CloudFormationType("AWS::GameLift::Fleet")
    data class Fleet(
        val BuildId: String,
        val Description: String? = null,
        val DesiredEC2Instances: String,
        val EC2InboundPermissions: List<GameLift.Fleet.EC2InboundPermissionProperty>? = null,
        val EC2InstanceType: String,
        val LogPaths: List<String>? = null,
        val MaxSize: String? = null,
        val MinSize: String? = null,
        val Name: String,
        val ServerLaunchParameters: String? = null,
        val ServerLaunchPath: String
    ) : ResourceProperties {

        data class EC2InboundPermissionProperty(
            val FromPort: String,
            val IpRange: String,
            val Protocol: String,
            val ToPort: String
        ) 

    }


}