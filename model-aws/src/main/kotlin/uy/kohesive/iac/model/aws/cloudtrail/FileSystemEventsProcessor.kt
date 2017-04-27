package uy.kohesive.iac.model.aws.cloudtrail

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy
import java.io.File
import java.io.FileInputStream
import java.util.zip.GZIPInputStream

class FileSystemEventsProcessor(
    override val eventsFilter: EventsFilter = EventsFilter.Empty,
    override val ignoreFailedRequests: Boolean = true,

    val eventsDir: File,
    val oneEventPerFile: Boolean,
    val gzipped: Boolean
) : EventsProcessor {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    override fun scrollEvents(): Sequence<CloudTrailEvent> = eventsDir.walkTopDown().filter { file ->
        if (gzipped) {
            file.name.endsWith(".json.gz")
        } else {
            file.name.endsWith(".json")
        }
    }.flatMap { file ->
        FileInputStream(file).let { fis ->
            when {
                gzipped -> GZIPInputStream(fis)
                else -> fis
            }
        }.use { inputStream ->
            JSON.readValue<Map<String, Any>>(inputStream).let { jsonMap ->
                parseEvents(jsonMap, file.nameWithoutExtension)
            }
        }
    }

    private fun parseEvents(jsonMap: Map<String, Any>, eventSourceFile: String): Sequence<CloudTrailEvent> =
        if (oneEventPerFile) {
            parseEvent(jsonMap, eventSourceFile)?.let { sequenceOf(it) } ?: emptySequence()
        } else {
            (jsonMap["Records"] as? List<Map<String, Any>>)?.mapIndexed { index, record ->
                parseEvent(record, eventSourceUri = "${eventSourceFile}_$index")
            }.orEmpty().filterNotNull().asSequence()
        }

}