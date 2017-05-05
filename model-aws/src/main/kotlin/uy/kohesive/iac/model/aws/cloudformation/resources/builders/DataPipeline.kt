package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.datapipeline.model.ActivatePipelineRequest
import com.amazonaws.services.datapipeline.model.CreatePipelineRequest
import com.amazonaws.services.datapipeline.model.PutPipelineDefinitionRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.DataPipeline

class DataPipelineResourcePropertiesBuilder : ResourcePropertiesBuilder<CreatePipelineRequest> {

    override val requestClazz = CreatePipelineRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreatePipelineRequest).let {
            val activateAction = relatedObjects.filterIsInstance<ActivatePipelineRequest>().firstOrNull()
            val definition     = relatedObjects.filterIsInstance<PutPipelineDefinitionRequest>().firstOrNull()
                ?: throw IllegalStateException("No related PutPipelineDefinitionRequest for pipelineId=${request.uniqueId} exists")

            DataPipeline.Pipeline(
                Description = request.description,
                Name        = request.name,
                Activate    = (activateAction != null).toString(),
                ParameterObjects = definition.parameterObjects?.map {
                    DataPipeline.Pipeline.ParameterObjectProperty(
                        Attributes = it.attributes.map {
                            DataPipeline.Pipeline.ParameterObjectProperty.AttributeProperty(
                                Key         = it.key,
                                StringValue = it.stringValue
                            )
                        },
                        Id = it.id
                    )
                },
                ParameterValues = (activateAction?.parameterValues.orEmpty() + definition.parameterValues.orEmpty()).map {
                    DataPipeline.Pipeline.ParameterValueProperty(
                        Id          = it.id,
                        StringValue = it.stringValue
                    )
                },
                PipelineObjects = definition.pipelineObjects.map {
                    DataPipeline.Pipeline.PipelineObjectProperty(
                        Fields = it.fields.map {
                            DataPipeline.Pipeline.PipelineObjectProperty.FieldProperty(
                                Key         = it.key,
                                StringValue = it.stringValue,
                                RefValue    = it.refValue
                            )
                        },
                        Id   = it.id,
                        Name = it.name
                    )
                },
                PipelineTags = request.tags?.map {
                    DataPipeline.Pipeline.PipelineTagProperty(
                        Key   = it.key,
                        Value = it.value
                    )
                }
            )
        }

}

