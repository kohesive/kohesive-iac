package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.util.DateUtils
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.cloudtrail.preprocessing.CloudTrailEventPreprocessors
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy
import java.io.File
import java.io.FileInputStream
import java.util.zip.GZIPInputStream

enum class EventsSource {
    Filesystem,
    CloudTrailAPI
}

open class FileSystemEventsProcessor(
    val eventsFilter: EventsFilter = EventsFilter.Empty,
    val eventsDir: File,
    val oneEventPerFile: Boolean,
    val gzipped: Boolean,
    val ignoreFailedRequests: Boolean = true
) : EventsProcessor {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

        val VersionedEventNameRegexp = "(.*)(\\d{4}(-|_)?\\d{2}(-|_)?\\d{2})".toRegex()
    }

    override fun <T> process(processor: (CloudTrailEvent) -> T): Sequence<T> = eventsDir.walkTopDown().filter { file ->
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
                parseEvents(jsonMap, file.nameWithoutExtension).filter { cloudTrailEvent ->
                    if (eventsFilter.isEmpty()) {
                        true
                    } else {
                        eventsFilter.startTime?.let { it.before(cloudTrailEvent.eventTime) } ?: true &&
                        eventsFilter.endTime?.let { it.after(cloudTrailEvent.eventTime) } ?: true
                    }
                }.map { cloudTrailEvent ->
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

    private fun parseEvents(jsonMap: Map<String, Any>, eventSourceFile: String): Sequence<CloudTrailEvent> =
        if (oneEventPerFile) {
            parseEvent(jsonMap, eventSourceFile)?.let { sequenceOf(it) } ?: emptySequence()
        } else {
            (jsonMap["Records"] as? List<Map<String, Any>>)?.mapIndexed { index, record ->
                parseEvent(record, eventSourceFile = "${eventSourceFile}_$index")
            }.orEmpty().filterNotNull().asSequence()
        }

    private fun parseEvent(jsonMap: Map<String, Any>, eventSourceFile: String): CloudTrailEvent? {
        val errorCode = jsonMap["errorCode"] as String?
        if (ignoreFailedRequests && errorCode != null) {
            return null
        }

        val eventId    = jsonMap["eventID"] as String
        val eventName  = jsonMap["eventName"] as String
        val apiVersion = jsonMap["apiVersion"] as? String
        val eventTime  = DateUtils.parseISO8601Date(jsonMap["eventTime"] as String)

        val (fixedEventName, fixedApiVersion) = if (VersionedEventNameRegexp.matches(eventName)) {
            VersionedEventNameRegexp.find(eventName)!!.groupValues.let {
                it[1] to it[2]
            }
        } else {
            eventName to apiVersion
        }

        return CloudTrailEventPreprocessors.preprocess(CloudTrailEvent(
            eventId         = eventId,
            eventSourceFile = eventSourceFile,
            eventTime       = eventTime,
            eventSource     = jsonMap["eventSource"] as String,
            eventName       = fixedEventName,
            apiVersion      = fixedApiVersion,
            request         = (jsonMap["requestParameters"] as? RequestMap).orEmpty()
        ))
    }

}