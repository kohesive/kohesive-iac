package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreams
import com.amazonaws.services.dynamodbv2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface DynamoDBStreamsIdentifiable : KohesiveIdentifiable {

}

interface DynamoDBStreamsEnabled : DynamoDBStreamsIdentifiable {
    val dynamoDBStreamsClient: AmazonDynamoDBStreams
    val dynamoDBStreamsContext: DynamoDBStreamsContext
    fun <T> withDynamoDBStreamsContext(init: DynamoDBStreamsContext.(AmazonDynamoDBStreams) -> T): T = dynamoDBStreamsContext.init(dynamoDBStreamsClient)
}

open class BaseDynamoDBStreamsContext(protected val context: IacContext) : DynamoDBStreamsEnabled by context {


}

@DslScope
class DynamoDBStreamsContext(context: IacContext) : BaseDynamoDBStreamsContext(context) {

}
