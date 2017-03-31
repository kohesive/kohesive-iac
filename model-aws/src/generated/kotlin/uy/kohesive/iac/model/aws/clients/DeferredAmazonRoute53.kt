package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.route53.AbstractAmazonRoute53
import com.amazonaws.services.route53.AmazonRoute53
import com.amazonaws.services.route53.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonRoute53(val context: IacContext) : AbstractAmazonRoute53(), AmazonRoute53 {

    override fun createHealthCheck(request: CreateHealthCheckRequest): CreateHealthCheckResult {
        return with (context) {
            request.registerWithAutoName()
            CreateHealthCheckResult().withHealthCheck(
                makeProxy<CreateHealthCheckRequest, HealthCheck>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateHealthCheckRequest::getCallerReference to HealthCheck::getCallerReference,
                        CreateHealthCheckRequest::getHealthCheckConfig to HealthCheck::getHealthCheckConfig
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createHostedZone(request: CreateHostedZoneRequest): CreateHostedZoneResult {
        return with (context) {
            request.registerWithAutoName()
            CreateHostedZoneResult().withHostedZone(
                makeProxy<CreateHostedZoneRequest, HostedZone>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateHostedZoneRequest::getName to HostedZone::getName,
                        CreateHostedZoneRequest::getCallerReference to HostedZone::getCallerReference
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createReusableDelegationSet(request: CreateReusableDelegationSetRequest): CreateReusableDelegationSetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateReusableDelegationSetResult().withDelegationSet(
                makeProxy<CreateReusableDelegationSetRequest, DelegationSet>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateReusableDelegationSetRequest::getCallerReference to DelegationSet::getCallerReference
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createTrafficPolicy(request: CreateTrafficPolicyRequest): CreateTrafficPolicyResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTrafficPolicyResult().withTrafficPolicy(
                makeProxy<CreateTrafficPolicyRequest, TrafficPolicy>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateTrafficPolicyRequest::getName to TrafficPolicy::getName,
                        CreateTrafficPolicyRequest::getDocument to TrafficPolicy::getDocument,
                        CreateTrafficPolicyRequest::getComment to TrafficPolicy::getComment
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createTrafficPolicyInstance(request: CreateTrafficPolicyInstanceRequest): CreateTrafficPolicyInstanceResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTrafficPolicyInstanceResult().withTrafficPolicyInstance(
                makeProxy<CreateTrafficPolicyInstanceRequest, TrafficPolicyInstance>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateTrafficPolicyInstanceRequest::getHostedZoneId to TrafficPolicyInstance::getHostedZoneId,
                        CreateTrafficPolicyInstanceRequest::getName to TrafficPolicyInstance::getName,
                        CreateTrafficPolicyInstanceRequest::getTTL to TrafficPolicyInstance::getTTL,
                        CreateTrafficPolicyInstanceRequest::getTrafficPolicyId to TrafficPolicyInstance::getTrafficPolicyId,
                        CreateTrafficPolicyInstanceRequest::getTrafficPolicyVersion to TrafficPolicyInstance::getTrafficPolicyVersion
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createTrafficPolicyVersion(request: CreateTrafficPolicyVersionRequest): CreateTrafficPolicyVersionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTrafficPolicyVersionResult().withTrafficPolicy(
                makeProxy<CreateTrafficPolicyVersionRequest, TrafficPolicy>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateTrafficPolicyVersionRequest::getId to TrafficPolicy::getId,
                        CreateTrafficPolicyVersionRequest::getDocument to TrafficPolicy::getDocument,
                        CreateTrafficPolicyVersionRequest::getComment to TrafficPolicy::getComment
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createVPCAssociationAuthorization(request: CreateVPCAssociationAuthorizationRequest): CreateVPCAssociationAuthorizationResult {
        return with (context) {
            request.registerWithAutoName()
            CreateVPCAssociationAuthorizationResult().withVPC(
                makeProxy<CreateVPCAssociationAuthorizationRequest, VPC>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonRoute53(context: IacContext) : BaseDeferredAmazonRoute53(context)
