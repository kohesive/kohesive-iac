package uy.kohesive.iac.examples.aws.lambdas

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder
import com.amazonaws.services.kinesis.model.PutRecordsRequest
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent

class SnsEventToKinesisStreamForwarding(val kinesis: AmazonKinesis = AmazonKinesisClientBuilder.defaultClient(),
                                        overridePartitionFieldName: String? = null,
                                        overrideKinesisOutputStreamEnvValue: String? = null)
    : KinesisForwardingLambda(overridePartitionFieldName, overrideKinesisOutputStreamEnvValue), RequestHandler<SNSEvent, Unit> {

    override fun handleRequest(input: SNSEvent, context: Context) {
        input.records.map { it.sns.message }.kinesisSafeJsonBatching { batch ->
            val sendBatch = batch.map { PutRecordsRequestEntry().withPartitionKey(it.first).withData(it.second) }
            val response = kinesis.putRecords(PutRecordsRequest().withStreamName(outputKinesisStream).withRecords(sendBatch))
            if (response.failedRecordCount > 0) {
                response.records.mapIndexed { i, item -> i to item }.filter { it.second.errorCode != null }.map { sendBatch[it.first] to it.second }
                // TODO: log errors, or do what, error queue?
                context.logger.log("Some records failed to write to the Kinesis stream") // TODO: log something more meaningful
            }
        }
    }
}