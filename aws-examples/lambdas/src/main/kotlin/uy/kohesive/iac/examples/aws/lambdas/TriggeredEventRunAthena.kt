package uy.kohesive.iac.examples.aws.lambdas

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.DriverManager
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class TriggeredEventRunAthena(val overrideCredentialsProviderWithProfile: String? = null,
                              val overrideS3OutputBucketEnvValue: String? = null,
                              val overrideOutputFilePrefixEnvValue: String? = null,
                              val overrideS3InputBucketEnvValue: String? = null,
                              val overrideInputBucketPrefix: String? = null,
                              val overrideAthenaRegion: String? = null) : RequestHandler<ScheduledEvent, AthenaDatasetResponse> {
    companion object {
        val ENV_SETTING_OUTPUT_S3_BUCKET = "OUTPUT_S3_BUCKET"
        val ENV_SETTING_OUTPUT_PREFIX = "OUTPUT_FILE_PREFIX"

        val ENV_SETTING_INPUT_S3_BUCKET = "INPUT_S3_BUCKET"
        val ENV_SETTING_INPUT_PREFIX = "INTPUT_FILE_PREFIX"

        val ENV_SETTING_ATHENA_REGION = "ATHENA_AWS_REGION"
    }

    val athenaS3OutputBucket = overrideS3OutputBucketEnvValue
            ?: System.getenv(ENV_SETTING_OUTPUT_S3_BUCKET)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_OUTPUT_S3_BUCKET} defining the S3 bucket for output")
    val athenaOutputFilePrefix = overrideOutputFilePrefixEnvValue
            ?: System.getenv(ENV_SETTING_OUTPUT_PREFIX)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_OUTPUT_PREFIX} defining the file prefix for output")

    val athenaS3InputBucket = overrideS3InputBucketEnvValue
            ?: System.getenv(ENV_SETTING_INPUT_S3_BUCKET)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_INPUT_S3_BUCKET} defining the S3 bucket for input")
    val athenaInputFilePrefix = overrideInputBucketPrefix
            ?: System.getenv(ENV_SETTING_INPUT_PREFIX)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_INPUT_PREFIX} defining the file prefix for input")

    val athenaRegion = overrideAthenaRegion
            ?: System.getenv(ENV_SETTING_ATHENA_REGION)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_ATHENA_REGION} defining the Athena AWS region")

    val athenaUrl = "jdbc:awsathena://athena.${athenaRegion}.amazonaws.com:443/"

    val defaultJdbcProps = Properties().apply {
        if (overrideCredentialsProviderWithProfile != null) {
            put("aws_credentials_provider_class", ProfileCredentialsProvider::class.java.name)
            put("aws_credentials_provider_arguments", overrideCredentialsProviderWithProfile)
        } else {
            put("aws_credentials_provider_class", EnvironmentVariableCredentialsProvider::class.java.name)
        }
    }

    override fun handleRequest(input: ScheduledEvent, context: Context): AthenaDatasetResponse {
        // TODO: make time a range, and go back to last processed (DynamoDB state) in case we had an outage/delay for data feed
        val parsedTime = Instant.from(DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC).parse(input.time))
        return processForEventTime(parsedTime, context.logger)
    }

    fun processForEventTime(time: Instant, logger: LambdaLogger?): AthenaDatasetResponse {
        val execTime = Instant.now()
        val execReversedNumericTime = "%020d".format(Long.MAX_VALUE - execTime.toEpochMilli())
        val execTimeAsString = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").format(execTime.atZone(ZoneOffset.UTC))
        val execUniqueness = UUID.randomUUID().toString()
        val execIdentifier = "$execReversedNumericTime-$execTimeAsString-$execUniqueness"

        // our output bucket will always sort the newest data to the top of the list when scanning the S3 prefix
        val outputLocation = "s3://${athenaS3OutputBucket}/${athenaOutputFilePrefix.trimStart('/').trimEnd('/')}/$execIdentifier/"

        val connectProps = Properties(defaultJdbcProps)
        connectProps.put("s3_staging_dir", outputLocation)

        val tempTableName = "notification_email_digest_temp_${execUniqueness.replace('-', '_')}"

        val targetHour = time.atOffset(ZoneOffset.UTC).truncatedTo(ChronoUnit.HOURS).minusHours(1)
        fun Int.twoDigits() = "%02d".format(this)
        val targetHourPrefix = "${targetHour.year}/${targetHour.month.value.twoDigits()}/${targetHour.dayOfMonth.twoDigits()}/${targetHour.hour.twoDigits()}"

        logger?.log("Processing target hour: ${targetHour} at prefix ${targetHourPrefix} to $outputLocation")

        // TODO: move to freemarker template?
        val createTempTableSql = """
            CREATE EXTERNAL TABLE `$tempTableName`(
              `email` string COMMENT 'from deserializer',
              `name` string COMMENT 'from deserializer',
              `message` string COMMENT 'from deserializer',
              `timestamp` string COMMENT 'from deserializer')
            ROW FORMAT SERDE
              'org.openx.data.jsonserde.JsonSerDe'
            LOCATION
              's3://${athenaS3InputBucket}/${athenaInputFilePrefix.trimStart('/').trimEnd('/')}/$targetHourPrefix/'

        """

        // TODO: move to freemarker template?
        val dropTempTableSql = """
            DROP TABLE `$tempTableName`
        """

        // TODO: range the query to only be the involved time frame

        // TODO: move to freemarker template?
        val querySql = """
                WITH base AS (
                    SELECT email, name, message, timestamp, parse_datetime(timestamp, 'yyyy-MM-dd''T''HH:mm:ss') as eventTime
                      FROM $tempTableName
                     ORDER BY eventTime
                ), hourBucketed AS (
                    SELECT *, date_trunc('hour', eventTime) as eventHour
                      FROM base
                ), mapped AS (
                    SELECT eventHour, eventTime, email, name, map(ARRAY['eventTime', 'message'], ARRAY[timestamp, message]) as event
                      FROM hourBucketed
                     ORDER BY eventTime
                ), rolledUp AS (
                    SELECT email, arbitrary(name) as name, count(*) as eventCount, array_agg(event) as events
                      FROM mapped
                     GROUP BY email
                )
                SELECT email, name, eventCount, cast(events AS JSON) as events FROM rolledUp
                 ORDER BY email
           """

        Class.forName("com.amazonaws.athena.jdbc.AthenaDriver")
        DriverManager.getConnection(athenaUrl, connectProps).use { db ->
            // let exceptions fall through for Lambda logging of errors
            db.createStatement().executeQuery(createTempTableSql)
            db.createStatement().executeQuery(querySql)
            db.createStatement().executeQuery(dropTempTableSql)
        }
        return AthenaDatasetResponse(outputLocation)
    }
}

data class AthenaDatasetResponse(val outputS3Location: String)

// TODO: the lambda events JAR is missing one for cloudwatch scheduled events, or I missed it.  Look for it again, this is a odd class just to get the time of the event.
class ScheduledEvent() {
    var version: String? = null
    var id: String? = null
    @get:JsonProperty("detail-type") var detailType: String? = null
    var source: String? = null
    var account: String? = null
    lateinit var time: String
    var region: String? = null
    var resources: List<String> = emptyList()
    var detail: Map<String, Any> = emptyMap()
}