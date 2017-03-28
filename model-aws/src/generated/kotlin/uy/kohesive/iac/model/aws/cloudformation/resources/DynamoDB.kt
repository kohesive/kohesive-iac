package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object DynamoDB {

    @CloudFormationType("AWS::DynamoDB::Table")
    data class Table(
        val AttributeDefinitions: List<DynamoDB.Table.AttributeDefinitionProperty>,
        val GlobalSecondaryIndexes: List<DynamoDB.Table.GlobalSecondaryIndexProperty>? = null,
        val KeySchema: List<DynamoDB.Table.KeySchemaProperty>,
        val LocalSecondaryIndexes: List<DynamoDB.Table.LocalSecondaryIndexProperty>? = null,
        val ProvisionedThroughput: Table.ProvisionedThroughputProperty,
        val StreamSpecification: Table.StreamSpecificationProperty? = null,
        val TableName: String? = null
    ) : ResourceProperties {

        data class AttributeDefinitionProperty(
            val AttributeName: String,
            val AttributeType: String
        ) 


        data class GlobalSecondaryIndexProperty(
            val IndexName: String,
            val KeySchema: List<DynamoDB.Table.KeySchemaProperty>,
            val Projection: Table.ProjectionProperty,
            val ProvisionedThroughput: Table.ProvisionedThroughputProperty
        ) 


        data class KeySchemaProperty(
            val AttributeName: String,
            val KeyType: String
        ) 


        data class ProjectionProperty(
            val NonKeyAttributes: List<String>? = null,
            val ProjectionType: String? = null
        ) 


        data class ProvisionedThroughputProperty(
            val ReadCapacityUnits: String,
            val WriteCapacityUnits: String
        ) 


        data class LocalSecondaryIndexProperty(
            val IndexName: String,
            val KeySchema: List<DynamoDB.Table.KeySchemaProperty>,
            val Projection: Table.ProjectionProperty
        ) 


        data class StreamSpecificationProperty(
            val StreamViewType: String
        ) 

    }


}