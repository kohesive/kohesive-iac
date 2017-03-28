package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object DataPipeline {

    @CloudFormationType("AWS::DataPipeline::Pipeline")
    data class Pipeline(
        val Activate: String? = null,
        val Description: String? = null,
        val Name: String,
        val ParameterObjects: List<DataPipeline.Pipeline.ParameterObjectProperty>? = null,
        val ParameterValues: List<DataPipeline.Pipeline.ParameterValueProperty>? = null,
        val PipelineObjects: List<DataPipeline.Pipeline.PipelineObjectProperty>,
        val PipelineTags: List<DataPipeline.Pipeline.PipelineTagProperty>? = null
    ) : ResourceProperties {

        data class ParameterObjectProperty(
            val Attributes: List<DataPipeline.Pipeline.ParameterObjectProperty.AttributeProperty>,
            val Id: String
        ) {

            data class AttributeProperty(
                val Key: String,
                val StringValue: String? = null
            ) 

        }


        data class ParameterValueProperty(
            val Id: String,
            val StringValue: String
        ) 


        data class PipelineObjectProperty(
            val Fields: List<DataPipeline.Pipeline.PipelineObjectProperty.FieldProperty>,
            val Id: String,
            val Name: String
        ) {

            data class FieldProperty(
                val Key: String,
                val RefValue: String? = null,
                val StringValue: String? = null
            ) 

        }


        data class PipelineTagProperty(
            val Key: String,
            val Value: String
        ) 

    }


}