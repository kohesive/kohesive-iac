package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.TableDescription
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.utils.DslScope

@DslScope
class DynamoDBContext(private val context: IacContext) : DynamoDBEnabled by context {

    fun createTable(tableName: String, init: CreateTableRequest.() -> Unit): TableDescription {
        return dynamoDBClient.createTable(CreateTableRequest().apply {
            withTableName(tableName)
            init()
        }).tableDescription
    }

}