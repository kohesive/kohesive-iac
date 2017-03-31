package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.redshift.AbstractAmazonRedshift
import com.amazonaws.services.redshift.AmazonRedshift
import com.amazonaws.services.redshift.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonRedshift(val context: IacContext) : AbstractAmazonRedshift(), AmazonRedshift {

    override fun createCluster(request: CreateClusterRequest): Cluster {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateClusterRequest, Cluster>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateClusterRequest::getClusterIdentifier to Cluster::getClusterIdentifier,
                    CreateClusterRequest::getNodeType to Cluster::getNodeType,
                    CreateClusterRequest::getMasterUsername to Cluster::getMasterUsername,
                    CreateClusterRequest::getDBName to Cluster::getDBName,
                    CreateClusterRequest::getAutomatedSnapshotRetentionPeriod to Cluster::getAutomatedSnapshotRetentionPeriod,
                    CreateClusterRequest::getClusterSubnetGroupName to Cluster::getClusterSubnetGroupName,
                    CreateClusterRequest::getAvailabilityZone to Cluster::getAvailabilityZone,
                    CreateClusterRequest::getPreferredMaintenanceWindow to Cluster::getPreferredMaintenanceWindow,
                    CreateClusterRequest::getClusterVersion to Cluster::getClusterVersion,
                    CreateClusterRequest::getAllowVersionUpgrade to Cluster::getAllowVersionUpgrade,
                    CreateClusterRequest::getNumberOfNodes to Cluster::getNumberOfNodes,
                    CreateClusterRequest::getPubliclyAccessible to Cluster::getPubliclyAccessible,
                    CreateClusterRequest::getEncrypted to Cluster::getEncrypted,
                    CreateClusterRequest::getTags to Cluster::getTags,
                    CreateClusterRequest::getKmsKeyId to Cluster::getKmsKeyId,
                    CreateClusterRequest::getEnhancedVpcRouting to Cluster::getEnhancedVpcRouting
                )
            )
        }
    }

    override fun createClusterParameterGroup(request: CreateClusterParameterGroupRequest): ClusterParameterGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateClusterParameterGroupRequest, ClusterParameterGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateClusterParameterGroupRequest::getParameterGroupName to ClusterParameterGroup::getParameterGroupName,
                    CreateClusterParameterGroupRequest::getParameterGroupFamily to ClusterParameterGroup::getParameterGroupFamily,
                    CreateClusterParameterGroupRequest::getDescription to ClusterParameterGroup::getDescription,
                    CreateClusterParameterGroupRequest::getTags to ClusterParameterGroup::getTags
                )
            )
        }
    }

    override fun createClusterSecurityGroup(request: CreateClusterSecurityGroupRequest): ClusterSecurityGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateClusterSecurityGroupRequest, ClusterSecurityGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateClusterSecurityGroupRequest::getClusterSecurityGroupName to ClusterSecurityGroup::getClusterSecurityGroupName,
                    CreateClusterSecurityGroupRequest::getDescription to ClusterSecurityGroup::getDescription,
                    CreateClusterSecurityGroupRequest::getTags to ClusterSecurityGroup::getTags
                )
            )
        }
    }

    override fun createClusterSnapshot(request: CreateClusterSnapshotRequest): Snapshot {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateClusterSnapshotRequest, Snapshot>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateClusterSnapshotRequest::getSnapshotIdentifier to Snapshot::getSnapshotIdentifier,
                    CreateClusterSnapshotRequest::getClusterIdentifier to Snapshot::getClusterIdentifier,
                    CreateClusterSnapshotRequest::getTags to Snapshot::getTags
                )
            )
        }
    }

    override fun createClusterSubnetGroup(request: CreateClusterSubnetGroupRequest): ClusterSubnetGroup {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateClusterSubnetGroupRequest, ClusterSubnetGroup>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateClusterSubnetGroupRequest::getClusterSubnetGroupName to ClusterSubnetGroup::getClusterSubnetGroupName,
                    CreateClusterSubnetGroupRequest::getDescription to ClusterSubnetGroup::getDescription,
                    CreateClusterSubnetGroupRequest::getTags to ClusterSubnetGroup::getTags
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
                    CreateEventSubscriptionRequest::getSeverity to EventSubscription::getSeverity,
                    CreateEventSubscriptionRequest::getEnabled to EventSubscription::getEnabled,
                    CreateEventSubscriptionRequest::getTags to EventSubscription::getTags
                )
            )
        }
    }

    override fun createHsmClientCertificate(request: CreateHsmClientCertificateRequest): HsmClientCertificate {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateHsmClientCertificateRequest, HsmClientCertificate>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateHsmClientCertificateRequest::getHsmClientCertificateIdentifier to HsmClientCertificate::getHsmClientCertificateIdentifier,
                    CreateHsmClientCertificateRequest::getTags to HsmClientCertificate::getTags
                )
            )
        }
    }

    override fun createHsmConfiguration(request: CreateHsmConfigurationRequest): HsmConfiguration {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateHsmConfigurationRequest, HsmConfiguration>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateHsmConfigurationRequest::getHsmConfigurationIdentifier to HsmConfiguration::getHsmConfigurationIdentifier,
                    CreateHsmConfigurationRequest::getDescription to HsmConfiguration::getDescription,
                    CreateHsmConfigurationRequest::getHsmIpAddress to HsmConfiguration::getHsmIpAddress,
                    CreateHsmConfigurationRequest::getHsmPartitionName to HsmConfiguration::getHsmPartitionName,
                    CreateHsmConfigurationRequest::getTags to HsmConfiguration::getTags
                )
            )
        }
    }

    override fun createSnapshotCopyGrant(request: CreateSnapshotCopyGrantRequest): SnapshotCopyGrant {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateSnapshotCopyGrantRequest, SnapshotCopyGrant>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateSnapshotCopyGrantRequest::getSnapshotCopyGrantName to SnapshotCopyGrant::getSnapshotCopyGrantName,
                    CreateSnapshotCopyGrantRequest::getKmsKeyId to SnapshotCopyGrant::getKmsKeyId,
                    CreateSnapshotCopyGrantRequest::getTags to SnapshotCopyGrant::getTags
                )
            )
        }
    }

    override fun createTags(request: CreateTagsRequest): CreateTagsResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTagsResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonRedshift(context: IacContext) : BaseDeferredAmazonRedshift(context)
