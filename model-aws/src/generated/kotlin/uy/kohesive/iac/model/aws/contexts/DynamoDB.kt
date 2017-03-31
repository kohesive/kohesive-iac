package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface DynamoDBIdentifiable : KohesiveIdentifiable {

}

interface DynamoDBEnabled : DynamoDBIdentifiable {
    val dynamoDBClient: AmazonDynamoDB
    val dynamoDBContext: DynamoDBContext
    fun <T> withDynamoDBContext(init: DynamoDBContext.(AmazonDynamoDB) -> T): T = dynamoDBContext.init(dynamoDBClient)
}

open class BaseDynamoDBContext(protected val context: IacContext) : DynamoDBEnabled by context {

    fun createTable(tableName: String, init: CreateTableRequest.() -> Unit): TableDescription {
        return dynamoDBClient.createTable(CreateTableRequest().apply {
            withTableName(tableName)
            init()
        }).tableDescription
    }


}

