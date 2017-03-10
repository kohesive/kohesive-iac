package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.TableDescription
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface DynamoDBIdentifiable : KohesiveIdentifiable {

}

interface DynamoDbEnabled : DynamoDBIdentifiable {
    val dynamoDbClient: AmazonDynamoDB
    val dynamoDbContext: DynamoDbContext
    fun <T> withDynamoDbContext(init: DynamoDbContext.(AmazonDynamoDB) -> T): T = dynamoDbContext.init(dynamoDbClient)
}

@DslScope
class DynamoDbContext(private val context: IacContext) : DynamoDbEnabled by context {

    fun createTable(tableName: String, init: CreateTableRequest.() -> Unit): TableDescription {
        return dynamoDbClient.createTable(CreateTableRequest().apply {
            withTableName(tableName)
            init()
        }).tableDescription
    }

}