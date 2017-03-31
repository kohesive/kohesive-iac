package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.rds.AbstractAmazonRDS
import com.amazonaws.services.rds.AmazonRDS
import com.amazonaws.services.rds.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonRDS(val context: IacContext) : AbstractAmazonRDS(), AmazonRDS {

    override fun addRoleToDBCluster(request: AddRoleToDBClusterRequest): AddRoleToDBClusterResult {
        return with (context) {
            request.registerWithAutoName()
            AddRoleToDBClusterResult().registerWithSameNameAs(request)
        }
    }

    override fun addSourceIdentifierToSubscription(request: AddSourceIdentifierToSubscriptionRequest): EventSubscription {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddSourceIdentifierToSubscriptionRequest, EventSubscription>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun addTagsToResource(request: AddTagsToResourceRequest): AddTagsToResourceResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsToResourceResult().registerWithSameNameAs(request)
        }
    }

    override fun createDBCluster(request: CreateDBClusterRequest): DBCluster {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDBClusterRequest, DBCluster>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDBClusterRequest::getAvailabilityZones to DBCluster::getAvailabilityZones,
                    CreateDBClusterRequest::getBackupRetentionPeriod to DBCluster::getBackupRetentionPeriod,
                    CreateDBClusterRequest::getCharacterSetName to DBCluster::getCharacterSetName,
                    CreateDBClusterRequest::getDatabaseName to DBCluster::getDatabaseName,
                    CreateDBClusterRequest::getDBClusterIdentifier to DBCluster::getDBClusterIdentifier,
                    CreateDBClusterRequest::getEngine to DBCluster::getEngine,
                    CreateDBClusterRequest::getEngineVersion to DBCluster::getEngineVersion,
                    CreateDBClusterRequest::getPort to DBCluster::getPort,
                    CreateDBClusterRequest::getMasterUsername to DBCluster::getMasterUsername,
                    CreateDBClusterRequest::getPreferredBackupWindow to DBCluster::getPreferredBackupWindow,
                    CreateDBClusterRequest::getPreferredMaintenanceWindow to DBCluster::getPreferredMaintenanceWindow,
                    CreateDBClusterRequest::getReplicationSourceIdentifier to DBCluster::getReplicationSourceIdentifier,
                    CreateDBClusterRequest::getStorageEncrypted to DBCluster::getStorageEncrypted,
                    CreateDBClusterRequest::getKmsKeyId to DBCluster::getKmsKeyId
                )
            )
        }
    }

    override fun createDBClusterParameterGroup(request: CreateDBClusterParameterGroupRequest): DBClusterParameterGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDBClusterParameterGroupRequest, DBClusterParameterGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDBClusterParameterGroupRequest::getDBClusterParameterGroupName to DBClusterParameterGroup::getDBClusterParameterGroupName,
                    CreateDBClusterParameterGroupRequest::getDBParameterGroupFamily to DBClusterParameterGroup::getDBParameterGroupFamily,
                    CreateDBClusterParameterGroupRequest::getDescription to DBClusterParameterGroup::getDescription
                )
            )
        }
    }

    override fun createDBClusterSnapshot(request: CreateDBClusterSnapshotRequest): DBClusterSnapshot {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDBClusterSnapshotRequest, DBClusterSnapshot>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDBClusterSnapshotRequest::getDBClusterSnapshotIdentifier to DBClusterSnapshot::getDBClusterSnapshotIdentifier,
                    CreateDBClusterSnapshotRequest::getDBClusterIdentifier to DBClusterSnapshot::getDBClusterIdentifier
                )
            )
        }
    }

    override fun createDBInstance(request: CreateDBInstanceRequest): DBInstance {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDBInstanceRequest, DBInstance>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDBInstanceRequest::getDBInstanceIdentifier to DBInstance::getDBInstanceIdentifier,
                    CreateDBInstanceRequest::getDBInstanceClass to DBInstance::getDBInstanceClass,
                    CreateDBInstanceRequest::getEngine to DBInstance::getEngine,
                    CreateDBInstanceRequest::getMasterUsername to DBInstance::getMasterUsername,
                    CreateDBInstanceRequest::getDBName to DBInstance::getDBName,
                    CreateDBInstanceRequest::getAllocatedStorage to DBInstance::getAllocatedStorage,
                    CreateDBInstanceRequest::getPreferredBackupWindow to DBInstance::getPreferredBackupWindow,
                    CreateDBInstanceRequest::getBackupRetentionPeriod to DBInstance::getBackupRetentionPeriod,
                    CreateDBInstanceRequest::getAvailabilityZone to DBInstance::getAvailabilityZone,
                    CreateDBInstanceRequest::getPreferredMaintenanceWindow to DBInstance::getPreferredMaintenanceWindow,
                    CreateDBInstanceRequest::getMultiAZ to DBInstance::getMultiAZ,
                    CreateDBInstanceRequest::getEngineVersion to DBInstance::getEngineVersion,
                    CreateDBInstanceRequest::getAutoMinorVersionUpgrade to DBInstance::getAutoMinorVersionUpgrade,
                    CreateDBInstanceRequest::getLicenseModel to DBInstance::getLicenseModel,
                    CreateDBInstanceRequest::getIops to DBInstance::getIops,
                    CreateDBInstanceRequest::getCharacterSetName to DBInstance::getCharacterSetName,
                    CreateDBInstanceRequest::getPubliclyAccessible to DBInstance::getPubliclyAccessible,
                    CreateDBInstanceRequest::getStorageType to DBInstance::getStorageType,
                    CreateDBInstanceRequest::getTdeCredentialArn to DBInstance::getTdeCredentialArn,
                    CreateDBInstanceRequest::getDBClusterIdentifier to DBInstance::getDBClusterIdentifier,
                    CreateDBInstanceRequest::getStorageEncrypted to DBInstance::getStorageEncrypted,
                    CreateDBInstanceRequest::getKmsKeyId to DBInstance::getKmsKeyId,
                    CreateDBInstanceRequest::getCopyTagsToSnapshot to DBInstance::getCopyTagsToSnapshot,
                    CreateDBInstanceRequest::getMonitoringInterval to DBInstance::getMonitoringInterval,
                    CreateDBInstanceRequest::getMonitoringRoleArn to DBInstance::getMonitoringRoleArn,
                    CreateDBInstanceRequest::getPromotionTier to DBInstance::getPromotionTier,
                    CreateDBInstanceRequest::getTimezone to DBInstance::getTimezone
                )
            )
        }
    }

    override fun createDBInstanceReadReplica(request: CreateDBInstanceReadReplicaRequest): DBInstance {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDBInstanceReadReplicaRequest, DBInstance>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDBInstanceReadReplicaRequest::getDBInstanceIdentifier to DBInstance::getDBInstanceIdentifier,
                    CreateDBInstanceReadReplicaRequest::getDBInstanceClass to DBInstance::getDBInstanceClass,
                    CreateDBInstanceReadReplicaRequest::getAvailabilityZone to DBInstance::getAvailabilityZone,
                    CreateDBInstanceReadReplicaRequest::getAutoMinorVersionUpgrade to DBInstance::getAutoMinorVersionUpgrade,
                    CreateDBInstanceReadReplicaRequest::getIops to DBInstance::getIops,
                    CreateDBInstanceReadReplicaRequest::getPubliclyAccessible to DBInstance::getPubliclyAccessible,
                    CreateDBInstanceReadReplicaRequest::getStorageType to DBInstance::getStorageType,
                    CreateDBInstanceReadReplicaRequest::getKmsKeyId to DBInstance::getKmsKeyId,
                    CreateDBInstanceReadReplicaRequest::getCopyTagsToSnapshot to DBInstance::getCopyTagsToSnapshot,
                    CreateDBInstanceReadReplicaRequest::getMonitoringInterval to DBInstance::getMonitoringInterval,
                    CreateDBInstanceReadReplicaRequest::getMonitoringRoleArn to DBInstance::getMonitoringRoleArn
                )
            )
        }
    }

    override fun createDBParameterGroup(request: CreateDBParameterGroupRequest): DBParameterGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDBParameterGroupRequest, DBParameterGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDBParameterGroupRequest::getDBParameterGroupName to DBParameterGroup::getDBParameterGroupName,
                    CreateDBParameterGroupRequest::getDBParameterGroupFamily to DBParameterGroup::getDBParameterGroupFamily,
                    CreateDBParameterGroupRequest::getDescription to DBParameterGroup::getDescription
                )
            )
        }
    }

    override fun createDBSecurityGroup(request: CreateDBSecurityGroupRequest): DBSecurityGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDBSecurityGroupRequest, DBSecurityGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDBSecurityGroupRequest::getDBSecurityGroupName to DBSecurityGroup::getDBSecurityGroupName,
                    CreateDBSecurityGroupRequest::getDBSecurityGroupDescription to DBSecurityGroup::getDBSecurityGroupDescription
                )
            )
        }
    }

    override fun createDBSnapshot(request: CreateDBSnapshotRequest): DBSnapshot {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDBSnapshotRequest, DBSnapshot>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDBSnapshotRequest::getDBSnapshotIdentifier to DBSnapshot::getDBSnapshotIdentifier,
                    CreateDBSnapshotRequest::getDBInstanceIdentifier to DBSnapshot::getDBInstanceIdentifier
                )
            )
        }
    }

    override fun createDBSubnetGroup(request: CreateDBSubnetGroupRequest): DBSubnetGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDBSubnetGroupRequest, DBSubnetGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDBSubnetGroupRequest::getDBSubnetGroupName to DBSubnetGroup::getDBSubnetGroupName,
                    CreateDBSubnetGroupRequest::getDBSubnetGroupDescription to DBSubnetGroup::getDBSubnetGroupDescription
                )
            )
        }
    }

    override fun createEventSubscription(request: CreateEventSubscriptionRequest): EventSubscription {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateEventSubscriptionRequest, EventSubscription>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateEventSubscriptionRequest::getSnsTopicArn to EventSubscription::getSnsTopicArn,
                    CreateEventSubscriptionRequest::getSourceType to EventSubscription::getSourceType,
                    CreateEventSubscriptionRequest::getEnabled to EventSubscription::getEnabled
                )
            )
        }
    }

    override fun createOptionGroup(request: CreateOptionGroupRequest): OptionGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateOptionGroupRequest, OptionGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateOptionGroupRequest::getOptionGroupName to OptionGroup::getOptionGroupName,
                    CreateOptionGroupRequest::getOptionGroupDescription to OptionGroup::getOptionGroupDescription,
                    CreateOptionGroupRequest::getEngineName to OptionGroup::getEngineName,
                    CreateOptionGroupRequest::getMajorEngineVersion to OptionGroup::getMajorEngineVersion
                )
            )
        }
    }


}

class DeferredAmazonRDS(context: IacContext) : BaseDeferredAmazonRDS(context)
