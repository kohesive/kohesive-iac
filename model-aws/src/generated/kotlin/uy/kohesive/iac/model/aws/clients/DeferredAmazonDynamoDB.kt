package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.dynamodbv2.AbstractAmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonDynamoDB(val context: IacContext) : AbstractAmazonDynamoDB(), AmazonDynamoDB {

    override fun createTable(request: CreateTableRequest): CreateTableResult {
        return with (context) {
            request.registerWithAutoName()
            CreateTableResult().withTableDescription(
                makeProxy<CreateTableRequest, TableDescription>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateTableRequest::getAttributeDefinitions to TableDescription::getAttributeDefinitions,
                        CreateTableRequest::getTableName to TableDescription::getTableName,
                        CreateTableRequest::getKeySchema to TableDescription::getKeySchema,
                        CreateTableRequest::getStreamSpecification to TableDescription::getStreamSpecification
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

