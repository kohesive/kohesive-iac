package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elastictranscoder.AbstractAmazonElasticTranscoder
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder
import com.amazonaws.services.elastictranscoder.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonElasticTranscoder(val context: IacContext) : AbstractAmazonElasticTranscoder(), AmazonElasticTranscoder {

    override fun createJob(request: CreateJobRequest): CreateJobResult {
        return with (context) {
            request.registerWithAutoName()
            CreateJobResult().withJob(
                makeProxy<CreateJobRequest, Job>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateJobRequest::getPipelineId to Job::getPipelineId,
                        CreateJobRequest::getInput to Job::getInput,
                        CreateJobRequest::getInputs to Job::getInputs,
                        CreateJobRequest::getOutputKeyPrefix to Job::getOutputKeyPrefix,
                        CreateJobRequest::getUserMetadata to Job::getUserMetadata
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createPipeline(request: CreatePipelineRequest): CreatePipelineResult {
        return with (context) {
            request.registerWithAutoName()
            CreatePipelineResult().withPipeline(
                makeProxy<CreatePipelineRequest, Pipeline>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreatePipelineRequest::getName to Pipeline::getName,
                        CreatePipelineRequest::getInputBucket to Pipeline::getInputBucket,
                        CreatePipelineRequest::getOutputBucket to Pipeline::getOutputBucket,
                        CreatePipelineRequest::getRole to Pipeline::getRole,
                        CreatePipelineRequest::getAwsKmsKeyArn to Pipeline::getAwsKmsKeyArn,
                        CreatePipelineRequest::getNotifications to Pipeline::getNotifications,
                        CreatePipelineRequest::getContentConfig to Pipeline::getContentConfig,
                        CreatePipelineRequest::getThumbnailConfig to Pipeline::getThumbnailConfig
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createPreset(request: CreatePresetRequest): CreatePresetResult {
        return with (context) {
            request.registerWithAutoName()
            CreatePresetResult().withPreset(
                makeProxy<CreatePresetRequest, Preset>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreatePresetRequest::getName to Preset::getName,
                        CreatePresetRequest::getDescription to Preset::getDescription,
                        CreatePresetRequest::getContainer to Preset::getContainer,
                        CreatePresetRequest::getAudio to Preset::getAudio,
                        CreatePresetRequest::getVideo to Preset::getVideo,
                        CreatePresetRequest::getThumbnails to Preset::getThumbnails
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonElasticTranscoder(context: IacContext) : BaseDeferredAmazonElasticTranscoder(context)
