package uy.kohesive.iac.examples.aws.lambdas

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder
import com.amazonaws.services.kinesisfirehose.model.PutRecordBatchRequest
import com.amazonaws.services.kinesisfirehose.model.Record
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent

class SnsEventToKinesisFirehoseForwarding(val kinesis: AmazonKinesisFirehose = AmazonKinesisFirehoseClientBuilder.defaultClient(),
                                          overridePartitionFieldName: String? = null,
                                          overrideKinesisOutputStreamEnvValue: String? = null)
    : KinesisForwardingLambda(overridePartitionFieldName, overrideKinesisOutputStreamEnvValue), RequestHandler<SNSEvent, Unit> {

    override fun handleRequest(input: SNSEvent, context: Context) {
        input.records.map { it.sns.message }.kinesisSafeJsonBatching { batch ->
            val sendBatch = batch.map { Record().withData(it.second) }
            val response = kinesis.putRecordBatch(PutRecordBatchRequest().withDeliveryStreamName(outputKinesisStream).withRecords(sendBatch))
            if (response.failedPutCount > 0) {
                response.requestResponses.mapIndexed { i, item -> i to item }.filter { it.second.errorCode != null }.map { sendBatch[it.first] to it.second }
                // TODO: log errors, or do what, error queue?
                context.logger.log("Some records failed to write to the Kinesis stream") // TODO: log something more meaningful
            }
        }
    }
}