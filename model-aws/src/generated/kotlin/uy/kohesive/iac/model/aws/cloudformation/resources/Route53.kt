package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object Route53 {

    @CloudFormationType("AWS::Route53::HealthCheck")
    data class HealthCheck(
        val HealthCheckConfig: HealthCheck.HealthCheckConfigProperty,
        val HealthCheckTags: List<Route53.HealthCheck.HealthCheckTagProperty>? = null
    ) : ResourceProperties {

        data class HealthCheckConfigProperty(
            val FailureThreshold: String? = null,
            val FullyQualifiedDomainName: String? = null,
            val IPAddress: String? = null,
            val Port: String? = null,
            val RequestInterval: String? = null,
            val ResourcePath: String? = null,
            val SearchString: String? = null,
            val Type: String
        ) 


        data class HealthCheckTagProperty(
            val Key: String,
            val Value: String
        ) 

    }

    @CloudFormationType("AWS::Route53::HostedZone")
    data class HostedZone(
        val HostedZoneConfig: HostedZone.HostedZoneConfigProperty? = null,
        val HostedZoneTags: List<Route53.HostedZone.HostedZoneTagProperty>? = null,
        val Name: String,
        val VPCs: List<Route53.HostedZone.HostedZoneVPCProperty>? = null
    ) : ResourceProperties {

        data class HostedZoneConfigProperty(
            val Comment: String? = null
        ) 


        data class HostedZoneTagProperty(
            val Key: String,
            val Value: String
        ) 


        data class HostedZoneVPCProperty(
            val VPCId: String,
            val VPCRegion: String
        ) 

    }

    @CloudFormationType("AWS::Route53::RecordSet")
    data class RecordSet(
        val AliasTarget: RecordSet.AliasTargetProperty? = null,
        val Comment: String? = null,
        val Failover: String? = null,
        val GeoLocation: RecordSet.GeoLocationProperty? = null,
        val HealthCheckId: String? = null,
        val HostedZoneId: String? = null,
        val HostedZoneName: String? = null,
        val Name: String,
        val Region: String? = null,
        val ResourceRecords: List<String>? = null,
        val SetIdentifier: String? = null,
        val TTL: String? = null,
        val Type: String,
        val Weight: String? = null
    ) : ResourceProperties {

        data class AliasTargetProperty(
            val DNSName: String,
            val EvaluateTargetHealth: String? = null,
            val HostedZoneId: String
        ) 


        data class GeoLocationProperty(
            val ContinentCode: String? = null,
            val CountryCode: String? = null,
            val SubdivisionCode: String? = null
        ) 

    }

    @CloudFormationType("AWS::Route53::RecordSetGroup")
    data class RecordSetGroup(
        val Comment: String? = null,
        val HostedZoneId: String? = null,
        val HostedZoneName: String? = null,
        val RecordSets: List<Route53.RecordSet>
    ) : ResourceProperties 


}