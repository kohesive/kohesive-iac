package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.dynamodbv2.AbstractAmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.CreateTableResult
import com.amazonaws.services.dynamodbv2.model.TableDescription
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

class DeferredAmazonDynamoDB(val context: IacContext) : AbstractAmazonDynamoDB(), AmazonDynamoDB {

    override fun createTable(request: CreateTableRequest): CreateTableResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTableResult().withTableDescription(
                makeProxy<CreateTableRequest, TableDescription>(context, getNameStrict(request), request, mapOf(
                    CreateTableRequest::getAttributeDefinitions to TableDescription::getAttributeDefinitions,
                    CreateTableRequest::getKeySchema            to TableDescription::getKeySchema,
                    CreateTableRequest::getTableName            to TableDescription::getTableName,
                    CreateTableRequest::getStreamSpecification  to TableDescription::getStreamSpecification
                ))
            ).apply { registerWithSameNameAs(request) }
        }
    }

}