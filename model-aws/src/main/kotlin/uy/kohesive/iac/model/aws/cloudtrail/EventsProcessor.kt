package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.services.cloudtrail.AWSCloudTrailClientBuilder
import com.amazonaws.services.cloudtrail.model.Event
import com.amazonaws.services.cloudtrail.model.LookupEventsRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy
import java.io.File

fun main(args: Array<String>) {
    val eventsDir = File("/Users/eliseyev/TMP/cloudtrail/")

    val awsModelProvider = AWSModelProvider()

    EventsProcessor().process(eventsDir) { event ->
        if (listOf("Create", "Put", "Attach", "Run").any { event.eventName.startsWith(it) }) {
            val serviceName = event.eventSource.split('.').first()

            val awsModel = try {
                awsModelProvider.getModel(serviceName, event.apiVersion)
            } catch (t: Throwable) {
                throw RuntimeException("Can't obtain an AWS model for $event", t)
            }

            AWSApiCallBuilder(awsModel, event).build()
        }
    }
}

class EventsProcessor {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    fun process(eventsDir: File, processor: (CloudTrailEvent) -> Unit) {
        eventsDir.listFiles { _, name -> name.endsWith(".json") }.forEach { jsonFile ->
            JSON.readValue<Map<String, Any>>(jsonFile).let { jsonMap ->
                processor(parseEvent(jsonMap, jsonFile.nameWithoutExtension))
            }
        }
    }

    private fun parseEvent(jsonMap: Map<String, Any>, eventId: String): CloudTrailEvent =
        CloudTrailEvent(
            eventId     = eventId,
            eventSource = jsonMap["eventSource"] as String,
            eventName   = jsonMap["eventName"] as String,
            apiVersion  = jsonMap["apiVersion"] as? String,
            request     = jsonMap["requestParameters"] as? Map<String, Any>
        )

}

fun storeEvents(outputDir: File) {
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
        File(outputDir, event.eventId + ".json").writeText(event.cloudTrailEvent)
    }
}