package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticmapreduce.AbstractAmazonElasticMapReduce
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce
import com.amazonaws.services.elasticmapreduce.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonElasticMapReduce(val context: IacContext) : AbstractAmazonElasticMapReduce(), AmazonElasticMapReduce {

    override fun addInstanceFleet(request: AddInstanceFleetRequest): AddInstanceFleetResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddInstanceFleetRequest, AddInstanceFleetResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    AddInstanceFleetRequest::getClusterId to AddInstanceFleetResult::getClusterId
                )
            )
        }
    }

    override fun addInstanceGroups(request: AddInstanceGroupsRequest): AddInstanceGroupsResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddInstanceGroupsRequest, AddInstanceGroupsResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    AddInstanceGroupsRequest::getJobFlowId to AddInstanceGroupsResult::getJobFlowId
                )
            )
        }
    }

    override fun addJobFlowSteps(request: AddJobFlowStepsRequest): AddJobFlowStepsResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddJobFlowStepsRequest, AddJobFlowStepsResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun addTags(request: AddTagsRequest): AddTagsResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsResult().registerWithSameNameAs(request)
        }
    }

    override fun createSecurityConfiguration(request: CreateSecurityConfigurationRequest): CreateSecurityConfigurationResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateSecurityConfigurationRequest, CreateSecurityConfigurationResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateSecurityConfigurationRequest::getName to CreateSecurityConfigurationResult::getName
                )
            )
        }
    }


}

class DeferredAmazonElasticMapReduce(context: IacContext) : BaseDeferredAmazonElasticMapReduce(context)
