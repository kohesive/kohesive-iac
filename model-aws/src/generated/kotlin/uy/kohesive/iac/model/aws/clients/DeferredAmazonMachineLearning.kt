package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.machinelearning.AbstractAmazonMachineLearning
import com.amazonaws.services.machinelearning.AmazonMachineLearning
import com.amazonaws.services.machinelearning.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonMachineLearning(val context: IacContext) : AbstractAmazonMachineLearning(), AmazonMachineLearning {

    override fun addTags(request: AddTagsRequest): AddTagsResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddTagsRequest, AddTagsResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    AddTagsRequest::getResourceId to AddTagsResult::getResourceId,
                    AddTagsRequest::getResourceType to AddTagsResult::getResourceType
                )
            )
        }
    }

    override fun createBatchPrediction(request: CreateBatchPredictionRequest): CreateBatchPredictionResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateBatchPredictionRequest, CreateBatchPredictionResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateBatchPredictionRequest::getBatchPredictionId to CreateBatchPredictionResult::getBatchPredictionId
                )
            )
        }
    }

    override fun createDataSourceFromRDS(request: CreateDataSourceFromRDSRequest): CreateDataSourceFromRDSResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDataSourceFromRDSRequest, CreateDataSourceFromRDSResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDataSourceFromRDSRequest::getDataSourceId to CreateDataSourceFromRDSResult::getDataSourceId
                )
            )
        }
    }

    override fun createDataSourceFromRedshift(request: CreateDataSourceFromRedshiftRequest): CreateDataSourceFromRedshiftResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDataSourceFromRedshiftRequest, CreateDataSourceFromRedshiftResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDataSourceFromRedshiftRequest::getDataSourceId to CreateDataSourceFromRedshiftResult::getDataSourceId
                )
            )
        }
    }

    override fun createDataSourceFromS3(request: CreateDataSourceFromS3Request): CreateDataSourceFromS3Result {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateDataSourceFromS3Request, CreateDataSourceFromS3Result>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateDataSourceFromS3Request::getDataSourceId to CreateDataSourceFromS3Result::getDataSourceId
                )
            )
        }
    }

    override fun createEvaluation(request: CreateEvaluationRequest): CreateEvaluationResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateEvaluationRequest, CreateEvaluationResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateEvaluationRequest::getEvaluationId to CreateEvaluationResult::getEvaluationId
                )
            )
        }
    }

    override fun createMLModel(request: CreateMLModelRequest): CreateMLModelResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateMLModelRequest, CreateMLModelResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateMLModelRequest::getMLModelId to CreateMLModelResult::getMLModelId
                )
            )
        }
    }

    override fun createRealtimeEndpoint(request: CreateRealtimeEndpointRequest): CreateRealtimeEndpointResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateRealtimeEndpointRequest, CreateRealtimeEndpointResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateRealtimeEndpointRequest::getMLModelId to CreateRealtimeEndpointResult::getMLModelId
                )
            )
        }
    }


}

class DeferredAmazonMachineLearning(context: IacContext) : BaseDeferredAmazonMachineLearning(context)
