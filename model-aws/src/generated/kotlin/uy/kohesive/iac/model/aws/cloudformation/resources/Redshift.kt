package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object Redshift {

    @CloudFormationType("AWS::Redshift::Cluster")
    data class Cluster(
        val AllowVersionUpgrade: String? = null,
        val AutomatedSnapshotRetentionPeriod: String? = null,
        val AvailabilityZone: String? = null,
        val ClusterParameterGroupName: String? = null,
        val ClusterSecurityGroups: List<String>? = null,
        val ClusterSubnetGroupName: String? = null,
        val ClusterType: String,
        val ClusterVersion: String? = null,
        val DBName: String,
        val ElasticIp: String? = null,
        val Encrypted: String? = null,
        val HsmClientCertificateIdentifier: String? = null,
        val HsmConfigurationIdentifier: String? = null,
        val KmsKeyId: String? = null,
        val MasterUsername: String,
        val MasterUserPassword: String,
        val NodeType: String,
        val NumberOfNodes: String? = null,
        val OwnerAccount: String? = null,
        val Port: String? = null,
        val PreferredMaintenanceWindow: String? = null,
        val PubliclyAccessible: String? = null,
        val SnapshotClusterIdentifier: String? = null,
        val SnapshotIdentifier: String? = null,
        val VpcSecurityGroupIds: List<String>? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::Redshift::ClusterParameterGroup")
    data class ClusterParameterGroup(
        val Description: String,
        val ParameterGroupFamily: String,
        val Parameters: List<Redshift.ClusterParameterGroup.ParameterProperty>? = null
    ) : ResourceProperties {

        data class ParameterProperty(
            val ParameterName: String,
            val ParameterValue: String
        ) 

    }

    @CloudFormationType("AWS::Redshift::ClusterSecurityGroup")
    data class ClusterSecurityGroup(
        val Description: String
    ) : ResourceProperties 

    @CloudFormationType("AWS::Redshift::ClusterSecurityGroupIngress")
    data class ClusterSecurityGroupIngress(
        val ClusterSecurityGroupName: String,
        val CIDRIP: String? = null,
        val EC2SecurityGroupName: String? = null,
        val EC2SecurityGroupOwnerId: String? = null
    ) : ResourceProperties 

    @CloudFormationType("AWS::Redshift::ClusterSubnetGroup")
    data class ClusterSubnetGroup(
        val Description: String,
        val SubnetIds: List<String>
    ) : ResourceProperties 


}