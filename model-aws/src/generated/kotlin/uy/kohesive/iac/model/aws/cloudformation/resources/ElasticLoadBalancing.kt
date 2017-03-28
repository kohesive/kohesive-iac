package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object ElasticLoadBalancing {

    @CloudFormationType("AWS::ElasticLoadBalancing::LoadBalancer")
    data class LoadBalancer(
        val AccessLoggingPolicy: LoadBalancer.AccessLoggingPolicyProperty? = null,
        val AppCookieStickinessPolicy: List<ElasticLoadBalancing.LoadBalancer.AppCookieStickinessPolicyProperty>? = null,
        val AvailabilityZones: List<String>? = null,
        val ConnectionDrainingPolicy: LoadBalancer.ConnectionDrainingPolicyProperty? = null,
        val ConnectionSettings: LoadBalancer.ConnectionSettingProperty? = null,
        val CrossZone: String? = null,
        val HealthCheck: LoadBalancer.HealthCheckProperty? = null,
        val Instances: List<String>? = null,
        val LBCookieStickinessPolicy: List<ElasticLoadBalancing.LoadBalancer.LBCookieStickinessPolicyProperty>? = null,
        val LoadBalancerName: String? = null,
        val Listeners: List<ElasticLoadBalancing.LoadBalancer.ListenerProperty>,
        val Policies: List<ElasticLoadBalancing.LoadBalancer.PolicyProperty>? = null,
        val Scheme: String? = null,
        val SecurityGroups: List<String>? = null,
        val Subnets: List<String>? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties {

        data class AccessLoggingPolicyProperty(
            val EmitInterval: String? = null,
            val Enabled: String,
            val S3BucketName: String,
            val S3BucketPrefix: String? = null
        ) 


        data class AppCookieStickinessPolicyProperty(
            val CookieName: String,
            val PolicyName: String
        ) 


        data class ConnectionDrainingPolicyProperty(
            val Enabled: String,
            val Timeout: String? = null
        ) 


        data class ConnectionSettingProperty(
            val IdleTimeout: String
        ) 


        data class HealthCheckProperty(
            val HealthyThreshold: String,
            val Interval: String,
            val Target: String,
            val Timeout: String,
            val UnhealthyThreshold: String
        ) 


        data class LBCookieStickinessPolicyProperty(
            val CookieExpirationPeriod: String? = null,
            val PolicyName: String
        ) 


        data class ListenerProperty(
            val InstancePort: String,
            val InstanceProtocol: String? = null,
            val LoadBalancerPort: String,
            val PolicyNames: List<String>? = null,
            val Protocol: String,
            val SSLCertificateId: String? = null
        ) 


        data class PolicyProperty(
            val Attributes: List<String>,
            val InstancePorts: List<String>? = null,
            val LoadBalancerPorts: List<String>? = null,
            val PolicyName: String,
            val PolicyType: String
        ) 

    }


}