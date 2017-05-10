package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.route53.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.Route53

class Route53RecordSetResourcePropertiesBuilder : ResourcePropertiesBuilder<ChangeResourceRecordSetsRequest> {

    override val requestClazz = ChangeResourceRecordSetsRequest::class

    override fun canBuildFrom(request: AmazonWebServiceRequest): Boolean
        = getRecordSetFromRequest(request) != null

    private fun getRecordSetFromRequest(request: AmazonWebServiceRequest): ResourceRecordSet? =
        (request as ChangeResourceRecordSetsRequest).changeBatch.changes?.firstOrNull {
            it.action == "CREATE" || it.action == "UPSERT"
        }?.resourceRecordSet

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>): Route53.RecordSet {
        val recordSetFromRequest = getRecordSetFromRequest(request) ?: throw IllegalStateException()
        return (request as ChangeResourceRecordSetsRequest).let {
            Route53.RecordSet(
                AliasTarget = recordSetFromRequest.aliasTarget?.let {
                    Route53.RecordSet.AliasTargetProperty(
                        DNSName              = it.dnsName,
                        EvaluateTargetHealth = it.evaluateTargetHealth?.toString(),
                        HostedZoneId         = it.hostedZoneId
                    )
                },
                HostedZoneId = it.hostedZoneId,
                Comment      = it.changeBatch.comment,
                Type         = recordSetFromRequest.type,
                Name         = recordSetFromRequest.name,
                Failover     = recordSetFromRequest.failover,
                GeoLocation  = recordSetFromRequest.geoLocation?.let {
                    Route53.RecordSet.GeoLocationProperty(
                        ContinentCode   = it.continentCode,
                        CountryCode     = it.countryCode,
                        SubdivisionCode = it.subdivisionCode
                    )
                },
                HealthCheckId   = recordSetFromRequest.healthCheckId,
                Region          = recordSetFromRequest.region,
                ResourceRecords = recordSetFromRequest.resourceRecords.map {
                    it.value
                },
                SetIdentifier = recordSetFromRequest.setIdentifier,
                TTL           = recordSetFromRequest.ttl?.toString(),
                Weight        = recordSetFromRequest.weight?.toString()
            )
        }
    }
}

class Route53HealthCheckResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateHealthCheckRequest> {

    override val requestClazz = CreateHealthCheckRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateHealthCheckRequest).let {
            Route53.HealthCheck(
                HealthCheckConfig = request.healthCheckConfig.let {
                    Route53.HealthCheck.HealthCheckConfigProperty(
                        FailureThreshold         = it.failureThreshold?.toString(),
                        Port                     = it.port?.toString(),
                        Type                     = it.type,
                        FullyQualifiedDomainName = it.fullyQualifiedDomainName,
                        IPAddress                = it.ipAddress,
                        RequestInterval          = it.requestInterval?.toString(),
                        ResourcePath             = it.resourcePath,
                        SearchString             = it.searchString
                    )
                },
                HealthCheckTags = relatedObjects.filterIsInstance<ChangeTagsForResourceRequest>().flatMap { it.addTags }.map {
                    Route53.HealthCheck.HealthCheckTagProperty(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

class Route53HostedZoneResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateHostedZoneRequest> {

    override val requestClazz = CreateHostedZoneRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateHostedZoneRequest).let {
            Route53.HostedZone(
                Name             = request.name,
                HostedZoneConfig = request.hostedZoneConfig.let {
                    Route53.HostedZone.HostedZoneConfigProperty(
                        Comment = it.comment
                    )
                },
                VPCs = relatedObjects.filterIsInstance<AssociateVPCWithHostedZoneRequest>().map {
                    Route53.HostedZone.HostedZoneVPCProperty(
                        VPCId     = it.vpc.vpcId,
                        VPCRegion = it.vpc.vpcRegion
                    )
                },
                HostedZoneTags = relatedObjects.filterIsInstance<ChangeTagsForResourceRequest>().flatMap { it.addTags }.map {
                    Route53.HostedZone.HostedZoneTagProperty(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

