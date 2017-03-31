package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.opsworks.AbstractAWSOpsWorks
import com.amazonaws.services.opsworks.AWSOpsWorks
import com.amazonaws.services.opsworks.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSOpsWorks(val context: IacContext) : AbstractAWSOpsWorks(), AWSOpsWorks {

    override fun attachElasticLoadBalancer(request: AttachElasticLoadBalancerRequest): AttachElasticLoadBalancerResult {
        return with (context) {
            request.registerWithAutoName()
            AttachElasticLoadBalancerResult().registerWithSameNameAs(request)
        }
    }

    override fun createApp(request: CreateAppRequest): CreateAppResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateAppRequest, CreateAppResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createDeployment(request: CreateDeploymentRequest): CreateDeploymentResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDeploymentRequest, CreateDeploymentResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createInstance(request: CreateInstanceRequest): CreateInstanceResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateInstanceRequest, CreateInstanceResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createLayer(request: CreateLayerRequest): CreateLayerResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateLayerRequest, CreateLayerResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createStack(request: CreateStackRequest): CreateStackResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateStackRequest, CreateStackResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createUserProfile(request: CreateUserProfileRequest): CreateUserProfileResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateUserProfileRequest, CreateUserProfileResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateUserProfileRequest::getIamUserArn to CreateUserProfileResult::getIamUserArn
                )
            )
        }
    }


}

class DeferredAWSOpsWorks(context: IacContext) : BaseDeferredAWSOpsWorks(context)
