package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.services.cloudtrail.AWSCloudTrailClientBuilder
import com.amazonaws.services.cloudtrail.model.Event
import com.amazonaws.services.cloudtrail.model.LookupEventsRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy
import java.io.File

fun main(args: Array<String>) {
    val eventsDir = File("/Users/eliseyev/TMP/cloudtrail/")

    val awsModelProvider = AWSModelProvider()

    EventsProcessor().process(eventsDir) { event ->
        if (listOf("Create", "Put", "Attach", "Run").any { event.eventName.startsWith(it) }) {
            val serviceName = event.eventSource.split('.').first()

            try {
                awsModelProvider.getModel(serviceName, event.apiVersion)
            } catch (t: Throwable) {
                throw RuntimeException("Error while processing $event", t)
            }
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
            JSON.readTree(jsonFile).let { jsonTree ->
                processor(parseEvent(jsonTree, jsonFile.nameWithoutExtension))
            }
        }
    }

    private fun parseEvent(jsonTree: JsonNode, eventId: String): CloudTrailEvent =
        CloudTrailEvent(
            eventId     = eventId,
            eventSource = jsonTree.get("eventSource").asText(),
            eventName   = jsonTree.get("eventName").asText(),
            apiVersion  = jsonTree.get("apiVersion")?.asText(),
            request     = jsonTree.get("requestParameters") as? ObjectNode
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