package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.gamelift.model.CreateAliasRequest
import com.amazonaws.services.gamelift.model.CreateBuildRequest
import com.amazonaws.services.gamelift.model.CreateFleetRequest
import com.amazonaws.services.gamelift.model.UpdateFleetCapacityRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.GameLift

class GameLiftAliasResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateAliasRequest> {

    override val requestClazz = CreateAliasRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateAliasRequest).let {
            GameLift.Alias(
                Description     = request.description,
                Name            = request.name,
                RoutingStrategy = request.routingStrategy.let {
                    GameLift.Alias.RoutingStrategyProperty(
                        FleetId = it.fleetId,
                        Type    = it.type,
                        Message = it.message
                    )
                }
            )
        }

}

class GameLiftBuildResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateBuildRequest> {

    override val requestClazz = CreateBuildRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateBuildRequest).let {
            GameLift.Build(
                Name            = request.name,
                StorageLocation = request.storageLocation?.let {
                    GameLift.Build.StorageLocationProperty(
                        Bucket  = it.bucket,
                        Key     = it.key,
                        RoleArn = it.roleArn
                    )
                },
                Version = request.version
            )
        }

}

class GameLiftFleetResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateFleetRequest> {

    override val requestClazz = CreateFleetRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateFleetRequest).let {
            val fleetCapacityRequest = relatedObjects.filterIsInstance<UpdateFleetCapacityRequest>().lastOrNull()

            GameLift.Fleet(
                BuildId               = request.buildId,
                Description           = request.description,
                EC2InboundPermissions = request.eC2InboundPermissions.map {
                    GameLift.Fleet.EC2InboundPermissionProperty(
                        FromPort = it.fromPort.toString(),
                        Protocol = it.protocol,
                        IpRange  = it.ipRange,
                        ToPort   = it.toPort.toString()
                    )
                },
                EC2InstanceType        = request.eC2InstanceType,
                LogPaths               = request.logPaths,
                Name                   = request.name,
                ServerLaunchParameters = request.serverLaunchParameters,
                ServerLaunchPath       = request.serverLaunchPath,
                DesiredEC2Instances    = fleetCapacityRequest?.desiredInstances.toString(),
                MaxSize                = fleetCapacityRequest?.maxSize?.toString(),
                MinSize                = fleetCapacityRequest?.minSize?.toString()
            )
        }

}

