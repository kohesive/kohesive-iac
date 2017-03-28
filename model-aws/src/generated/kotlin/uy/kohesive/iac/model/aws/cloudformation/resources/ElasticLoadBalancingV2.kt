package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object ElasticLoadBalancingV2 {

    @CloudFormationType("AWS::ElasticLoadBalancingV2::Listener")
    data class Listener(
        val Certificates: List<ElasticLoadBalancingV2.Listener.CertificateProperty>? = null,
        val DefaultActions: List<ElasticLoadBalancingV2.Listener.DefaultActionProperty>,
        val LoadBalancerArn: String,
        val Port: String,
        val Protocol: String,
        val SslPolicy: String? = null
    ) : ResourceProperties {

        data class CertificateProperty(
            val CertificateArn: String? = null
        ) 


        data class DefaultActionProperty(
            val TargetGroupArn: String,
            val Type: String
        ) 

    }

    @CloudFormationType("AWS::ElasticLoadBalancingV2::ListenerRule")
    data class ListenerRule(
        val Actions: List<ElasticLoadBalancingV2.ListenerRule.ActionProperty>,
        val Conditions: List<ElasticLoadBalancingV2.ListenerRule.ConditionProperty>,
        val ListenerArn: String,
        val Priority: String
    ) : ResourceProperties {

        data class ActionProperty(
            val TargetGroupArn: String,
            val Type: String
        ) 


        data class ConditionProperty(
            val Field: String? = null,
            val Values: List<String>? = null
        ) 

    }

    @CloudFormationType("AWS::ElasticLoadBalancingV2::LoadBalancer")
    data class LoadBalancer(
        val LoadBalancerAttributes: List<ElasticLoadBalancingV2.LoadBalancer.LoadBalancerAttributeProperty>? = null,
        val Name: String? = null,
        val Scheme: String? = null,
        val SecurityGroups: List<String>? = null,
        val Subnets: List<String>,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties {

        data class LoadBalancerAttributeProperty(
            val Key: String? = null,
            val Value: String? = null
        ) 

    }

    @CloudFormationType("AWS::ElasticLoadBalancingV2::TargetGroup")
    data class TargetGroup(
        val HealthCheckIntervalSeconds: String? = null,
        val HealthCheckPath: String? = null,
        val HealthCheckPort: String? = null,
        val HealthCheckProtocol: String? = null,
        val HealthCheckTimeoutSeconds: String? = null,
        val HealthyThresholdCount: String? = null,
        val Matcher: TargetGroup.MatcherProperty? = null,
        val Name: String? = null,
        val Port: String,
        val Protocol: String,
        val Tags: List<CloudFormation.ResourceTag>? = null,
        val TargetGroupAttributes: List<ElasticLoadBalancingV2.TargetGroup.TargetGroupAttributeProperty>? = null,
        val TargetGroupFullName: String? = null,
        val Targets: List<ElasticLoadBalancingV2.TargetGroup.TargetDescriptionProperty>? = null,
        val UnhealthyThresholdCount: String? = null,
        val VpcId: String
    ) : ResourceProperties {

        data class MatcherProperty(
            val HttpCode: String? = null
        ) 


        data class TargetGroupAttributeProperty(
            val Key: String? = null,
            val Value: String? = null
        ) 


        data class TargetDescriptionProperty(
            val Id: String,
            val Port: String? = null
        ) 

    }


}