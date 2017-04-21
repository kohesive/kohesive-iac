package uy.kohesive.iac.model.aws.cloudtrail

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.cloudtrail.preprocessing.CloudTrailEventPreprocessors
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy
import java.io.File
import java.io.FileInputStream
import java.util.zip.GZIPInputStream

class EventsProcessor(
    val eventsDir: File,
    val oneEventPerFile: Boolean,
    val gzipped: Boolean
) {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

        val VersionedEventNameRegexp = "(.*)(\\d{4}(-|_)?\\d{2}(-|_)?\\d{2})".toRegex()
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