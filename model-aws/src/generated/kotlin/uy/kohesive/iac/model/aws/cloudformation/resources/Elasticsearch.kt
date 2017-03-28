package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object Elasticsearch {

    @CloudFormationType("AWS::Elasticsearch::Domain")
    data class Domain(
        val AccessPolicies: Any? = null,
        val AdvancedOptions: Any? = null,
        val DomainName: String? = null,
        val EBSOptions: Domain.EBSOptionProperty? = null,
        val ElasticsearchClusterConfig: Domain.ElasticsearchClusterConfigProperty? = null,
        val ElasticsearchVersion: String? = null,
        val SnapshotOptions: Domain.SnapshotOptionProperty? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties {

        data class EBSOptionProperty(
            val EBSEnabled: String? = null,
            val Iops: String? = null,
            val VolumeSize: String? = null,
            val VolumeType: String? = null
        ) 


        data class ElasticsearchClusterConfigProperty(
            val DedicatedMasterCount: String? = null,
            val DedicatedMasterEnabled: String? = null,
            val DedicatedMasterType: String? = null,
            val InstanceCount: String? = null,
            val InstanceType: String? = null,
            val ZoneAwarenessEnabled: String? = null
        ) 


        data class SnapshotOptionProperty(
            val AutomatedSnapshotStartHour: String? = null
        ) 

    }


}