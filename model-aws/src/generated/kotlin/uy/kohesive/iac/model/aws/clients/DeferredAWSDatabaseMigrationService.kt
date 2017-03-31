package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.databasemigrationservice.AbstractAWSDatabaseMigrationService
import com.amazonaws.services.databasemigrationservice.AWSDatabaseMigrationService
import com.amazonaws.services.databasemigrationservice.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSDatabaseMigrationService(val context: IacContext) : AbstractAWSDatabaseMigrationService(), AWSDatabaseMigrationService {

    override fun createEndpoint(request: CreateEndpointRequest): CreateEndpointResult {
        return with (context) {
            request.registerWithAutoName()
            CreateEndpointResult().withEndpoint(
                makeProxy<CreateEndpointRequest, Endpoint>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateEndpointRequest::getEndpointIdentifier to Endpoint::getEndpointIdentifier,
                        CreateEndpointRequest::getEndpointType to Endpoint::getEndpointType,
                        CreateEndpointRequest::getEngineName to Endpoint::getEngineName,
                        CreateEndpointRequest::getUsername to Endpoint::getUsername,
                        CreateEndpointRequest::getServerName to Endpoint::getServerName,
                        CreateEndpointRequest::getPort to Endpoint::getPort,
                        CreateEndpointRequest::getDatabaseName to Endpoint::getDatabaseName,
                        CreateEndpointRequest::getExtraConnectionAttributes to Endpoint::getExtraConnectionAttributes,
                        CreateEndpointRequest::getKmsKeyId to Endpoint::getKmsKeyId,
                        CreateEndpointRequest::getCertificateArn to Endpoint::getCertificateArn,
                        CreateEndpointRequest::getSslMode to Endpoint::getSslMode
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createReplicationInstance(request: CreateReplicationInstanceRequest): CreateReplicationInstanceResult {
        return with (context) {
            request.registerWithAutoName()
            CreateReplicationInstanceResult().withReplicationInstance(
                makeProxy<CreateReplicationInstanceRequest, ReplicationInstance>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateReplicationInstanceRequest::getReplicationInstanceIdentifier to ReplicationInstance::getReplicationInstanceIdentifier,
                        CreateReplicationInstanceRequest::getReplicationInstanceClass to ReplicationInstance::getReplicationInstanceClass,
                        CreateReplicationInstanceRequest::getAllocatedStorage to ReplicationInstance::getAllocatedStorage,
                        CreateReplicationInstanceRequest::getAvailabilityZone to ReplicationInstance::getAvailabilityZone,
                        CreateReplicationInstanceRequest::getPreferredMaintenanceWindow to ReplicationInstance::getPreferredMaintenanceWindow,
                        CreateReplicationInstanceRequest::getMultiAZ to ReplicationInstance::getMultiAZ,
                        CreateReplicationInstanceRequest::getEngineVersion to ReplicationInstance::getEngineVersion,
                        CreateReplicationInstanceRequest::getAutoMinorVersionUpgrade to ReplicationInstance::getAutoMinorVersionUpgrade,
                        CreateReplicationInstanceRequest::getKmsKeyId to ReplicationInstance::getKmsKeyId,
                        CreateReplicationInstanceRequest::getPubliclyAccessible to ReplicationInstance::getPubliclyAccessible
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createReplicationSubnetGroup(request: CreateReplicationSubnetGroupRequest): CreateReplicationSubnetGroupResult {
        return with (context) {
            request.registerWithAutoName()
            CreateReplicationSubnetGroupResult().withReplicationSubnetGroup(
                makeProxy<CreateReplicationSubnetGroupRequest, ReplicationSubnetGroup>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateReplicationSubnetGroupRequest::getReplicationSubnetGroupIdentifier to ReplicationSubnetGroup::getReplicationSubnetGroupIdentifier,
                        CreateReplicationSubnetGroupRequest::getReplicationSubnetGroupDescription to ReplicationSubnetGroup::getReplicationSubnetGroupDescription
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createReplicationTask(request: CreateReplicationTaskRequest): CreateReplicationTaskResult {
        return with (context) {
            request.registerWithAutoName()
            CreateReplicationTaskResult().withReplicationTask(
                makeProxy<CreateReplicationTaskRequest, ReplicationTask>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateReplicationTaskRequest::getReplicationTaskIdentifier to ReplicationTask::getReplicationTaskIdentifier,
                        CreateReplicationTaskRequest::getSourceEndpointArn to ReplicationTask::getSourceEndpointArn,
                        CreateReplicationTaskRequest::getTargetEndpointArn to ReplicationTask::getTargetEndpointArn,
                        CreateReplicationTaskRequest::getReplicationInstanceArn to ReplicationTask::getReplicationInstanceArn,
                        CreateReplicationTaskRequest::getMigrationType to ReplicationTask::getMigrationType,
                        CreateReplicationTaskRequest::getTableMappings to ReplicationTask::getTableMappings,
                        CreateReplicationTaskRequest::getReplicationTaskSettings to ReplicationTask::getReplicationTaskSettings
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSDatabaseMigrationService(context: IacContext) : BaseDeferredAWSDatabaseMigrationService(context)
