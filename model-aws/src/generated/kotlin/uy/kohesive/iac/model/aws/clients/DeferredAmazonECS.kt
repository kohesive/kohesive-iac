package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.ecs.AbstractAmazonECS
import com.amazonaws.services.ecs.AmazonECS
import com.amazonaws.services.ecs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonECS(val context: IacContext) : AbstractAmazonECS(), AmazonECS {

    override fun createCluster(request: CreateClusterRequest): CreateClusterResult {
        return with (context) {
            request.registerWithAutoName()
            CreateClusterResult().withCluster(
                makeProxy<CreateClusterRequest, Cluster>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateClusterRequest::getClusterName to Cluster::getClusterName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createService(request: CreateServiceRequest): CreateServiceResult {
        return with (context) {
            request.registerWithAutoName()
            CreateServiceResult().withService(
                makeProxy<CreateServiceRequest, Service>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateServiceRequest::getServiceName to Service::getServiceName,
                        CreateServiceRequest::getLoadBalancers to Service::getLoadBalancers,
                        CreateServiceRequest::getDesiredCount to Service::getDesiredCount,
                        CreateServiceRequest::getTaskDefinition to Service::getTaskDefinition,
                        CreateServiceRequest::getDeploymentConfiguration to Service::getDeploymentConfiguration,
                        CreateServiceRequest::getPlacementConstraints to Service::getPlacementConstraints,
                        CreateServiceRequest::getPlacementStrategy to Service::getPlacementStrategy
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonECS(context: IacContext) : BaseDeferredAmazonECS(context)
