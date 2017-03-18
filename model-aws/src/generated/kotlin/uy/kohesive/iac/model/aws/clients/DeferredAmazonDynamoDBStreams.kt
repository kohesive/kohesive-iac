package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.dynamodbv2.AbstractAmazonDynamoDBStreams
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreams
import com.amazonaws.services.dynamodbv2.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonDynamoDBStreams(val context: IacContext) : AbstractAmazonDynamoDBStreams(), AmazonDynamoDBStreams {

}

class DeferredAmazonDynamoDBStreams(context: IacContext) : BaseDeferredAmazonDynamoDBStreams(context)