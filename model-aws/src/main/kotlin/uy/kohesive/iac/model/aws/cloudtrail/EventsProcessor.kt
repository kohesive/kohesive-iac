package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.services.cloudtrail.AWSCloudTrailClientBuilder
import com.amazonaws.services.cloudtrail.model.Event
import com.amazonaws.services.cloudtrail.model.LookupEventsRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.cloudtrail.preprocessing.CloudTrailEventPreprocessors
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy
import java.io.File
import java.io.FileInputStream
import java.util.zip.GZIPInputStream

fun main(args: Array<String>) {
    val awsModelProvider = AWSModelProvider()

    EventsProcessor(
//        eventsDir       = File("/Users/eliseyev/Downloads/CloudTrail/"),
        eventsDir       = File("/Users/eliseyev/Downloads/CloudTrail/us-east-1/2016/04/21/"),
        oneEventPerFile = false,
        gzipped         = true
    ).process { event ->
        if (listOf("Create", "Put", "Attach", "Run", "Set").any { event.eventName.startsWith(it) }) {
            val serviceName = event.eventSource.split('.').first()

            val awsModel = try {
                awsModelProvider.getModel(serviceName, event.apiVersion)
            } catch (t: Throwable) {
                throw RuntimeException("Can't obtain an AWS model for $event", t)
            }

            AWSApiCallBuilder(awsModel, event).build()
        } else {
            null
        }
    }.filterNotNull().forEach(::println)
}

class EventsProcessor(
    val eventsDir: File,
    val oneEventPerFile: Boolean,
    val gzipped: Boolean
) {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

        val VersionedEventNameRegexp = "(.*)(\\d{4}(-|_)\\d{2}(-|_)\\d{2})".toRegex()
    }

    fun <T> process(processor: (CloudTrailEvent) -> T): Sequence<T> = eventsDir.walkTopDown().filter { file ->
        if (gzipped) {
            file.name.endsWith(".json.gz")
        } else {
            file.name.endsWith(".json")
        }
    }.flatMap { file ->
        FileInputStream(file).let { fis ->
            when {
                gzipped -> GZIPInputStream(fis)
                else    -> fis
            }
        }.use { inputStream ->
            JSON.readValue<Map<String, Any>>(inputStream).let { jsonMap ->
                parseEvents(jsonMap, file.nameWithoutExtension).map { cloudTrailEvent ->
                    try {
                        processor(cloudTrailEvent)
                    } catch (t: Throwable) {
                        Thread.sleep(100)
                        throw RuntimeException("Error while processing event $cloudTrailEvent\nat ${file.path}", t)
                    }
                }
            }
        }
    }

    private fun parseEvents(jsonMap: Map<String, Any>, eventId: String): Sequence<CloudTrailEvent> =
        if (oneEventPerFile) {
            sequenceOf(parseEvent(jsonMap, eventId))
        } else {
            (jsonMap["Records"] as? List<Map<String, Any>>)?.mapIndexed { index, record ->
                parseEvent(record, eventId = "${eventId}_$index")
            }.orEmpty().asSequence()
        }

    private fun parseEvent(jsonMap: Map<String, Any>, eventId: String): CloudTrailEvent {
        val eventName  = jsonMap["eventName"] as String
        val apiVersion = jsonMap["apiVersion"] as? String

        val (fixedEventName, fixedApiVersion) = if (VersionedEventNameRegexp.matches(eventName)) {
            VersionedEventNameRegexp.find(eventName)!!.groupValues.let {
                it[1] to it[2]
            }
        } else {
            eventName to apiVersion
        }

        return CloudTrailEventPreprocessors.preprocess(CloudTrailEvent(
            eventId     = eventId,
            eventSource = jsonMap["eventSource"] as String,
            eventName   = fixedEventName,
            apiVersion  = fixedApiVersion,
            request     = (jsonMap["requestParameters"] as? RequestMap).orEmpty()
        ))
    }

}

fun fetchEvents(outputDir: File) {
    val cloudTrailClient = AWSCloudTrailClientBuilder.defaultClient()
    val firstLookup      = cloudTrailClient.lookupEvents()

    var nextToken: String? = firstLookup.nextToken
    val events: MutableList<Event> = firstLookup.events

    while (nextToken != null) {
        val nextLookup = cloudTrailClient.lookupEvents(LookupEventsRequest().withNextToken(nextToken))
        events.addAll(nextLookup.events)
        nextToken = nextLookup.nextToken
    }

    events.forEach { event ->
        val filename = event.eventId + ".json"
        File(outputDir, filename).writeText(event.cloudTrailEvent)
        println(filename)
    }
}