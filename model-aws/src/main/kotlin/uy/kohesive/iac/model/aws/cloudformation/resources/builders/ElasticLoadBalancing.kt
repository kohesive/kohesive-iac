package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.elasticloadbalancing.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.CloudFormation
import uy.kohesive.iac.model.aws.cloudformation.resources.ElasticLoadBalancing

class ElasticLoadBalancingLoadBalancerResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateLoadBalancerRequest> {

    override val requestClazz = CreateLoadBalancerRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateLoadBalancerRequest).let {
            val modifyAttributesRequests = relatedObjects.filterIsInstance<ModifyLoadBalancerAttributesRequest>().reversed().map {
                it.loadBalancerAttributes
            }

            ElasticLoadBalancing.LoadBalancer(
                AccessLoggingPolicy = modifyAttributesRequests.mapNotNull { it.accessLog }.firstOrNull()?.let {
                    ElasticLoadBalancing.LoadBalancer.AccessLoggingPolicyProperty(
                        EmitInterval   = it.emitInterval?.toString(),
                        S3BucketName   = it.s3BucketName,
                        Enabled        = it.enabled.toString(),
                        S3BucketPrefix = it.s3BucketPrefix
                    )
                },
                AppCookieStickinessPolicy = relatedObjects.filterIsInstance<CreateAppCookieStickinessPolicyRequest>().map {
                    ElasticLoadBalancing.LoadBalancer.AppCookieStickinessPolicyProperty(
                        CookieName = it.cookieName,
                        PolicyName = it.policyName
                    )
                },
                AvailabilityZones        = request.availabilityZones,
                ConnectionDrainingPolicy = modifyAttributesRequests.mapNotNull { it.connectionDraining }.firstOrNull()?.let {
                    ElasticLoadBalancing.LoadBalancer.ConnectionDrainingPolicyProperty(
                        Enabled = it.enabled.toString(),
                        Timeout = it.timeout?.toString()
                    )
                },
                ConnectionSettings = modifyAttributesRequests.mapNotNull { it.connectionSettings }.firstOrNull()?.let {
                    ElasticLoadBalancing.LoadBalancer.ConnectionSettingProperty(
                        IdleTimeout = it.idleTimeout.toString()
                    )
                },
                CrossZone = modifyAttributesRequests.mapNotNull { it.crossZoneLoadBalancing }.firstOrNull()?.let {
                    it.enabled?.toString()
                },
                HealthCheck = relatedObjects.filterIsInstance<ConfigureHealthCheckRequest>().firstOrNull()?.healthCheck?.let {
                    ElasticLoadBalancing.LoadBalancer.HealthCheckProperty(
                        HealthyThreshold   = it.healthyThreshold.toString(),
                        Timeout            = it.timeout.toString(),
                        Interval           = it.interval.toString(),
                        Target             = it.target,
                        UnhealthyThreshold = it.toString()
                    )
                },
                Instances = relatedObjects.filterIsInstance<RegisterInstancesWithLoadBalancerRequest>().flatMap { it.instances }.map {
                    it.instanceId
                }.distinct(),
                LBCookieStickinessPolicy = relatedObjects.filterIsInstance<CreateLBCookieStickinessPolicyRequest>().map {
                    ElasticLoadBalancing.LoadBalancer.LBCookieStickinessPolicyProperty(
                        CookieExpirationPeriod = it.cookieExpirationPeriod?.toString(),
                        PolicyName             = it.policyName
                    )
                },
                Listeners = request.listeners.map {
                    ElasticLoadBalancing.LoadBalancer.ListenerProperty(
                        InstancePort     = it.instancePort.toString(),
                        Protocol         = it.protocol,
                        InstanceProtocol = it.instanceProtocol,
                        LoadBalancerPort = it.loadBalancerPort.toString(),
                        SSLCertificateId = it.sslCertificateId
                    )
                },
                LoadBalancerName = request.loadBalancerName,
                Policies         = relatedObjects.filterIsInstance<CreateLoadBalancerPolicyRequest>().map {
                    ElasticLoadBalancing.LoadBalancer.PolicyProperty(
                        Attributes = it.policyAttributes.map {
                            ElasticLoadBalancing.LoadBalancer.PolicyPropertyAttribute(
                                Name  = it.attributeName,
                                Value = it.attributeValue
                            )
                        },
                        PolicyName = it.policyName,
                        PolicyType = it.policyTypeName
                        // TODO: 'InstancePorts' and 'LoadBalancerPorts' are not in this request
                    )
                },
                Scheme           = request.scheme,
                SecurityGroups   = request.securityGroups,
                Subnets          = request.subnets,
                Tags             = request.tags?.map {
                    CloudFormation.ResourceTag(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

