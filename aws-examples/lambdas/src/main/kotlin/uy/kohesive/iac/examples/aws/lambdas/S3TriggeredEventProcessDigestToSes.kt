package uy.kohesive.iac.examples.aws.lambdas

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.S3Event
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import nl.komponents.kovenant.all
import nl.komponents.kovenant.task
import nl.komponents.kovenant.then
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import uy.klutter.core.collections.lazyBatch
import java.io.File
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: have this push to SQS queue, which then sends each person.  Too high of chance of failures here processing within the Lambda

class S3TriggeredEventProcessDigestToSes(val overrideCredentialsProviderWithProfile: String? = null,
                                         val overrideSesAwsRegion: String? = null,
                                         val overrideAthenaAwsRegion: String? = null,
                                         val overrideSenderEmail: String? = null) : RequestHandler<S3Event, Unit> {
    companion object {
        val ENV_SETTING_SES_REGION = "SES_AWS_REGION"
        val ENV_SETTING_ATHENA_REGION = "ATHENA_AWS_REGION"
        val ENV_SETTING_SENDER_EMAIL = "SENDER_EMAIL"

        val templateBaseName = "social-notifications-digest"
        val templateLanguage = "en" // TODO: from each user's settings

        val JSON = ObjectMapper()
        val sesService = SesTemplatedEmailService()
    }

    val sesAwsRegion = overrideSesAwsRegion
            ?: System.getenv(ENV_SETTING_SES_REGION)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_SES_REGION} defining the SES region for sending email")

    val athenaAwsRegion = overrideAthenaAwsRegion
            ?: System.getenv(ENV_SETTING_ATHENA_REGION)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_ATHENA_REGION} defining the Athena region")


    val awsCredentials = overrideCredentialsProviderWithProfile?.let { ProfileCredentialsProvider(it) }
            ?: EnvironmentVariableCredentialsProvider()

    val senderEmail = overrideSenderEmail
            ?: System.getenv(ENV_SETTING_SENDER_EMAIL)
            ?: throw IllegalStateException("Missing environment variable ${ENV_SETTING_SENDER_EMAIL} defining the sender email")


    val sesClient = AmazonSimpleEmailServiceClient.builder().apply {
        credentials = awsCredentials
        region = sesAwsRegion
    }.build()

    val s3Client = AmazonS3Client.builder().apply {
        credentials = awsCredentials
        region = athenaAwsRegion
    }.build()

    override fun handleRequest(input: S3Event, context: Context) {
        input.records.forEach { event ->
            val s3Bucket = event.s3.bucket.name
            val objKey = event.s3.`object`.key
            processOneS3File(s3Bucket, objKey)
        }
    }

    fun processOneS3File(s3Bucket: String, objKey: String) {
        var lastSuccessfulLine = 0 // in case of failure, we need to know

        try {
            // always download S3 objects fast, never put anything in the way
            val tempFile = File.createTempFile("social-digest-${UUID.randomUUID()}-", ".csv").apply { deleteOnExit() }
            try {
                // TODO: add retry, sometimes S3 has temporary problems
                s3Client.getObject(GetObjectRequest(s3Bucket, objKey), tempFile)

                // file is one of:
                // "eventHour","email","name","eventCount","_col4"
                // "eventHour","email","name","eventCount","events"
                CSVParser.parse(tempFile, Charsets.UTF_8, CSVFormat.DEFAULT).use { parser ->
                    val eventEmailField = 0
                    val eventNameField = 1
                    val eventEventCountField = 2
                    val eventEventsField = 3

                    parser.asSequence().drop(1).map { record ->
                        val events = JSON.readValue<List<SocialEvent>>(record[eventEventsField], object : TypeReference<List<SocialEvent>>() {})

                        EmailModel(email = record[eventEmailField],
                                name = record[eventNameField],
                                events = events.sortedBy { it.eventTime })
                    }.lazyBatch(10) { batch ->
                        val sendTasks = batch.map { item ->
                            task {
                                // TODO: configure send Name, subject line
                                /*
                                sesService.sendEmail(sesClient, item.name, item.email, "Xyz", senderEmail,
                                        "Hi ${item.name}, your friends are active on Xyz",
                                        "$templateBaseName.$templateLanguage", item)
                                        */
                            }
                        }.toList()
                        all(sendTasks, cancelOthersOnError = false).then {
                            // noop, all emails sent!
                        }.fail {
                            // TODO: on error, we need to put the file and lastSuccessfulLine into SQS queue to try continuing from there later
                            //       we don't want a retry causing this to send duplicate emails
                        }.get() // force wait
                    }
                }
            } finally {
                tempFile.delete()
            }
        } catch (ex: Exception) {
            // TODO: on error, we need to put the file and lastSuccessfulLine into SQS queue to try continuing from there later
            //       we don't want a retry causing this to send duplicate emails
            throw ex
        }
    }


    class EmailModel(val email: String, val name: String, val events: List<SocialEvent>)
    class SocialEvent() {
        lateinit @get:JsonProperty("eventTime") var eventTimeStr: String
        lateinit var message: String
        @get:JsonIgnore val eventTime: Instant get() = Instant.from(DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC).parse("${eventTimeStr}Z"))
        // TODO: this should be formatted according to the receiving user's local settings, not the servers
        @get:JsonIgnore val eventTimeFormatted: String get() = DateTimeFormatter.ofPattern("EEEE, hh:mm").withZone(ZoneOffset.UTC).format(eventTime)
    }
}
