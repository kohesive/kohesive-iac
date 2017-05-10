package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.redshift.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.Redshift

class RedshiftClusterSecurityGroupIngressResourcePropertiesBuilder : ResourcePropertiesBuilder<AuthorizeClusterSecurityGroupIngressRequest> {

    override val requestClazz = AuthorizeClusterSecurityGroupIngressRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as AuthorizeClusterSecurityGroupIngressRequest).let {
            Redshift.ClusterSecurityGroupIngress(
                ClusterSecurityGroupName = it.clusterSecurityGroupName,
                EC2SecurityGroupName     = it.eC2SecurityGroupName,
                EC2SecurityGroupOwnerId  = it.eC2SecurityGroupOwnerId,
                CIDRIP                   = it.cidrip
            )
        }
}

// TODO: cluster may also be cloned - mind 'OwnerAccount', 'SnapshotClusterIdentifier', 'SnapshotIdentifier'
class RedshiftClusterResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateClusterRequest> {

    override val requestClazz = CreateClusterRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateClusterRequest).let {
            Redshift.Cluster(
                AllowVersionUpgrade              = request.allowVersionUpgrade?.toString(),
                AutomatedSnapshotRetentionPeriod = request.automatedSnapshotRetentionPeriod?.toString(),
                AvailabilityZone                 = request.availabilityZone,
                ClusterParameterGroupName        = request.clusterParameterGroupName,
                ClusterSecurityGroups            = request.clusterSecurityGroups,
                ClusterSubnetGroupName           = request.clusterSubnetGroupName,
                ClusterType                      = request.clusterType,
                ClusterVersion                   = request.clusterVersion,
                DBName                           = request.dbName,
                ElasticIp                        = request.elasticIp,
                Encrypted                        = request.encrypted?.toString(),
                HsmClientCertificateIdentifier   = request.hsmClientCertificateIdentifier,
                HsmConfigurationIdentifier       = request.hsmConfigurationIdentifier,
                KmsKeyId                         = request.kmsKeyId,
                MasterUserPassword               = request.masterUserPassword,
                MasterUsername                   = request.masterUsername,
                NodeType                         = request.nodeType,
                NumberOfNodes                    = request.numberOfNodes?.toString(),
                Port                             = request.port?.toString(),
                PreferredMaintenanceWindow       = request.preferredMaintenanceWindow,
                PubliclyAccessible               = request.publiclyAccessible?.toString(),
                VpcSecurityGroupIds              = request.vpcSecurityGroupIds
            )
        }

}

class RedshiftClusterParameterGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateClusterParameterGroupRequest> {

    override val requestClazz = CreateClusterParameterGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateClusterParameterGroupRequest).let {
            Redshift.ClusterParameterGroup(
                Description          = request.description,
                ParameterGroupFamily = request.parameterGroupFamily,
                Parameters           = relatedObjects.filterIsInstance<ModifyClusterParameterGroupRequest>().lastOrNull()?.parameters?.map {
                    Redshift.ClusterParameterGroup.ParameterProperty(
                        ParameterName  = it.parameterName,
                        ParameterValue = it.parameterValue
                    )
                }
            )
        }

}

class RedshiftClusterSecurityGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateClusterSecurityGroupRequest> {

    override val requestClazz = CreateClusterSecurityGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateClusterSecurityGroupRequest).let {
            Redshift.ClusterSecurityGroup(
                Description = request.description
            )
        }

}

class RedshiftClusterSubnetGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateClusterSubnetGroupRequest> {

    override val requestClazz = CreateClusterSubnetGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateClusterSubnetGroupRequest).let {
            Redshift.ClusterSubnetGroup(
                Description = request.description,
                SubnetIds   = request.subnetIds
            )
        }

}

