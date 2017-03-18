package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.dynamodbv2.AbstractAmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonDynamoDB(val context: IacContext) : AbstractAmazonDynamoDB(), AmazonDynamoDB {

}
