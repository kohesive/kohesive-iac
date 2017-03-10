package uy.kohesive.iac.model.aws.cloudformation.resources

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder

class DynamoDBTableResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateTableRequest> {

    override val requestClazz = CreateTableRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateTableRequest).let {
            DynamoDBTableResourceProperties(
                AttributeDefinitions = request.attributeDefinitions?.map {
                    AttributeDefinition(
                        AttributeName = it.attributeName,
                        AttributeType = it.attributeType
                    )
                },
                KeySchema = request.keySchema?.toCfKeySchema(),
                TableName = request.tableName,
                ProvisionedThroughput  = request.provisionedThroughput?.toCfProvisionedThroughput(),
                GlobalSecondaryIndexes = request.globalSecondaryIndexes?.map {
                    GlobalSecondaryIndex(
                        IndexName  = it.indexName,
                        KeySchema  = it.keySchema?.toCfKeySchema(),
                        Projection = it.projection?.toCfProjection(),
                        ProvisionedThroughput = it.provisionedThroughput?.toCfProvisionedThroughput()
                    )
                },
                LocalSecondaryIndexes = request.localSecondaryIndexes?.map {
                    LocalSecondaryIndex(
                        IndexName  = it.indexName,
                        KeySchema  = it.keySchema?.toCfKeySchema(),
                        Projection = it.projection?.toCfProjection()
                    )
                }
            )
        }
}

fun com.amazonaws.services.dynamodbv2.model.Projection.toCfProjection() = Projection(
    NonKeyAttributes = this.nonKeyAttributes,
    ProjectionType   = this.projectionType
)

fun com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput.toCfProvisionedThroughput() = ProvisionedThroughput(
    ReadCapacityUnits  = this.readCapacityUnits?.toString(),
    WriteCapacityUnits = this.writeCapacityUnits?.toString()
)

fun List<KeySchemaElement>.toCfKeySchema() = map {
    KeySchema(
        AttributeName = it.attributeName,
        KeyType       = it.keyType
    )
}

data class Projection(
    val NonKeyAttributes: List<String>?,
    val ProjectionType: String?
)

data class DynamoDBTableResourceProperties(
    val TableName: String?,
    val AttributeDefinitions: List<AttributeDefinition>?,
    val KeySchema: List<KeySchema>?,
    val ProvisionedThroughput: ProvisionedThroughput?,
    val GlobalSecondaryIndexes: List<GlobalSecondaryIndex>?,
    val LocalSecondaryIndexes: List<LocalSecondaryIndex>?
) : ResourceProperties

data class KeySchema(
    val AttributeName: String,
    val KeyType: String
)

data class AttributeDefinition(
    val AttributeName: String,
    val AttributeType: String
)

data class ProvisionedThroughput(
    val ReadCapacityUnits: String?,
    val WriteCapacityUnits: String?
)

data class GlobalSecondaryIndex(
    val IndexName: String?,
    val KeySchema: List<KeySchema>?,
    val Projection: Projection?,
    val ProvisionedThroughput: ProvisionedThroughput?
)

data class LocalSecondaryIndex(
    val IndexName: String?,
    val KeySchema: List<KeySchema>?,
    val Projection: Projection?
)