package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.codedeploy.AbstractAmazonCodeDeploy
import com.amazonaws.services.codedeploy.AmazonCodeDeploy
import com.amazonaws.services.codedeploy.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonCodeDeploy(val context: IacContext) : AbstractAmazonCodeDeploy(), AmazonCodeDeploy {

    override fun addTagsToOnPremisesInstances(request: AddTagsToOnPremisesInstancesRequest): AddTagsToOnPremisesInstancesResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsToOnPremisesInstancesResult().registerWithSameNameAs(request)
        }
    }

    override fun createApplication(request: CreateApplicationRequest): CreateApplicationResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateApplicationRequest, CreateApplicationResult>(
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

    override fun createDeploymentConfig(request: CreateDeploymentConfigRequest): CreateDeploymentConfigResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDeploymentConfigRequest, CreateDeploymentConfigResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createDeploymentGroup(request: CreateDeploymentGroupRequest): CreateDeploymentGroupResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDeploymentGroupRequest, CreateDeploymentGroupResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonCodeDeploy(context: IacContext) : BaseDeferredAmazonCodeDeploy(context)
