package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.elasticloadbalancingv2.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.ElasticLoadBalancingV2

class ElasticLoadBalancingV2ListenerRuleResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateRuleRequest> {

    override val requestClazz = CreateRuleRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateRuleRequest).let {
            ElasticLoadBalancingV2.ListenerRule(
                Actions = it.actions.map {
                    ElasticLoadBalancingV2.ListenerRule.ActionProperty(
                        TargetGroupArn = it.targetGroupArn,
                        Type           = it.type
                    )
                },
                Conditions = it.conditions.map {
                    ElasticLoadBalancingV2.ListenerRule.ConditionProperty(
                        Field  = it.field,
                        Values = it.values
                    )
                },
                ListenerArn = it.listenerArn,
                Priority    = it.priority.toString()
            )
        }
}

class ElasticLoadBalancingV2ListenerResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateListenerRequest> {

    override val requestClazz = CreateListenerRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateListenerRequest).let {
            ElasticLoadBalancingV2.Listener(
                Certificates = request.certificates.map {
                    ElasticLoadBalancingV2.Listener.CertificateProperty(
                        CertificateArn = it.certificateArn
                    )
                },
                DefaultActions = request.defaultActions.map {
                    ElasticLoadBalancingV2.Listener.DefaultActionProperty(
                        TargetGroupArn = it.targetGroupArn,
                        Type           = it.type
                    )
                },
                LoadBalancerArn = request.loadBalancerArn,
                Port            = request.port.toString(),
                Protocol        = request.protocol,
                SslPolicy       = request.sslPolicy
            )
        }

}

class ElasticLoadBalancingV2LoadBalancerResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateLoadBalancerRequest> {

    override val requestClazz = CreateLoadBalancerRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateLoadBalancerRequest).let {
            ElasticLoadBalancingV2.LoadBalancer(
                Name                   = request.name,
                LoadBalancerAttributes = relatedObjects.filterIsInstance<ModifyLoadBalancerAttributesRequest>().flatMap { it.attributes }.map {
                    ElasticLoadBalancingV2.LoadBalancer.LoadBalancerAttributeProperty(
                        Key   = it.key,
                        Value = it.value
                    )
                },
                Scheme         = request.scheme,
                SecurityGroups = request.securityGroups,
                Subnets        = request.subnets,
                Tags           = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

class ElasticLoadBalancingV2TargetGroupResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateTargetGroupRequest> {

    override val requestClazz = CreateTargetGroupRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateTargetGroupRequest).let {
            ElasticLoadBalancingV2.TargetGroup(
                HealthCheckIntervalSeconds = request.healthCheckIntervalSeconds?.toString(),
                HealthCheckPath            = request.healthCheckPath,
                HealthCheckPort            = request.healthCheckPort,
                HealthCheckProtocol        = request.healthCheckProtocol,
                HealthCheckTimeoutSeconds  = request.healthCheckTimeoutSeconds?.toString(),
                HealthyThresholdCount      = request.healthyThresholdCount?.toString(),
                Matcher                    = request.matcher?.let {
                    ElasticLoadBalancingV2.TargetGroup.MatcherProperty(
                        HttpCode = it.httpCode
                    )
                },
                Name     = request.name,
                Port     = request.port.toString(),
                Protocol = request.protocol,
                UnhealthyThresholdCount = request.unhealthyThresholdCount?.toString(),
                VpcId = request.vpcId,
                Tags = relatedObjects.filterIsInstance<AddTagsRequest>().flatMap { it.tags }.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                },
                TargetGroupAttributes = relatedObjects.filterIsInstance<ModifyTargetGroupAttributesRequest>().flatMap { it.attributes }.map {
                    ElasticLoadBalancingV2.TargetGroup.TargetGroupAttributeProperty(
                        Key   = it.key,
                        Value = it.value
                    )
                },
                Targets = relatedObjects.filterIsInstance<RegisterTargetsRequest>().flatMap { it.targets }.map {
                    ElasticLoadBalancingV2.TargetGroup.TargetDescriptionProperty(
                        Id   = it.id,
                        Port = it.port?.toString()
                    )
                }
            )
        }

}

