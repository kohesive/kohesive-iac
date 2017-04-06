package uy.kohesive.iac.examples.aws.lambdas

import com.fasterxml.jackson.databind.ObjectMapper
import uy.klutter.core.collections.batchWhile
import java.nio.ByteBuffer

private typealias PartitionedBuffer = Pair<String, ByteBuffer>

abstract class KinesisForwardingLambda(overridePartitionFieldName: String? = null, overrideKinesisOutputStreamEnvValue: String? = null) {
    companion object {
        val ENV_SETTING_OUTPUT_KINESIS_STREAM = "OUTPUT_KINESIS_STREAM"
        val ENV_SETTING_PARTITION_FIELD_NAME = "PARTITION_FIELD_NAME"

        val maxRecordsPerBatch = 500
        val maxRecordSize = 1024 // TODO: 1KB is by IEC or SI standard?
        val maxBatchSize = 400 * 1024 * 1024
        val JSON = ObjectMapper()
    }

    protected val outputKinesisStream = overrideKinesisOutputStreamEnvValue
            ?: System.getenv(ENV_SETTING_OUTPUT_KINESIS_STREAM)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_OUTPUT_KINESIS_STREAM} defining the Kinesis stream for output")

    protected val partitionFieldName = overridePartitionFieldName
            ?: System.getenv(ENV_SETTING_PARTITION_FIELD_NAME)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_PARTITION_FIELD_NAME} defining the partition key for the Kinesis stream")

    protected fun String.oneJsonPerLine(): String = "${this.ensureSingleLine().trim()}\n"

    // TODO: we don't check per record exceeding maxRecordSize

    protected fun List<String>.kinesisSafeJsonBatching(action: (List<PartitionedBuffer>) -> Unit): Unit {
        var totalSize = 0
        this.map {
            val partitionKey = JSON.readTree(it).path(partitionFieldName).takeIf { !it.isMissingNode }?.asText() ?: "no-partition"
            val cleanedJson = it.oneJsonPerLine().toByteArray()
            partitionKey to cleanedJson
        }.batchWhile(maxRecordsPerBatch) { totalSize += it.second.size; totalSize < maxBatchSize }
                .forEach { batch -> action(batch.map { it.first to ByteBuffer.wrap(it.second) }) }
    }
}