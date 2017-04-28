package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.services.cloudtrail.AWSCloudTrail
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class CloudTrailAPIEventsProcessor(
    override val eventsFilter: EventsFilter = EventsFilter.Empty,
    override val ignoreFailedRequests: Boolean = true,

    val awsClient: AWSCloudTrail
) : EventsProcessor {

    companion object {
        val JSON = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    override fun scrollEvents(): Sequence<CloudTrailEvent> = awsClient.eventsSequence(eventsFilter).map { record ->
        FileSystemEventsProcessor.JSON.readValue<Map<String, Any>>(record.cloudTrailEvent).let { jsonMap ->
            parseEvent(jsonMap)
        }
    }.filterNotNull()

}