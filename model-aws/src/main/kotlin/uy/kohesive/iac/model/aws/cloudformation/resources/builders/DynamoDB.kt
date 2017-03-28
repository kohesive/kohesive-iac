package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.Projection
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.DynamoDB

class DynamoDBTableResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateTableRequest> {

    override val requestClazz = CreateTableRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateTableRequest).let {
            DynamoDB.Table(
                AttributeDefinitions = request.attributeDefinitions.map {
                    DynamoDB.Table.AttributeDefinitionProperty(
                        AttributeName = it.attributeName,
                        AttributeType = it.attributeType
                    )
                },
                KeySchema = request.keySchema.toCfKeySchema(),
                //                TableName = request.tableName,
                ProvisionedThroughput  = request.provisionedThroughput.toCfProvisionedThroughput(),
                GlobalSecondaryIndexes = request.globalSecondaryIndexes?.map {
                    DynamoDB.Table.GlobalSecondaryIndexProperty(
                        IndexName             = it.indexName,
                        KeySchema             = it.keySchema.toCfKeySchema(),
                        Projection            = it.projection.toCfProjection(),
                        ProvisionedThroughput = it.provisionedThroughput.toCfProvisionedThroughput()
                    )
                },
                LocalSecondaryIndexes = request.localSecondaryIndexes?.map {
                    DynamoDB.Table.LocalSecondaryIndexProperty(
                        IndexName  = it.indexName,
                        KeySchema  = it.keySchema.toCfKeySchema(),
                        Projection = it.projection.toCfProjection()
                    )
                }
            )
        }
}

fun Projection.toCfProjection() = DynamoDB.Table.ProjectionProperty(
        NonKeyAttributes = this.nonKeyAttributes,
        ProjectionType   = this.projectionType
)

fun ProvisionedThroughput.toCfProvisionedThroughput() = DynamoDB.Table.ProvisionedThroughputProperty(
        ReadCapacityUnits  = this.readCapacityUnits.toString(),
        WriteCapacityUnits = this.writeCapacityUnits.toString()
)

fun List<KeySchemaElement>.toCfKeySchema() = map {
    DynamoDB.Table.KeySchemaProperty(
        AttributeName = it.attributeName,
        KeyType       = it.keyType
    )
}
