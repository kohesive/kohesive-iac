package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.util.DateUtils
import uy.kohesive.iac.model.aws.cloudtrail.preprocessing.CloudTrailEventPreprocessors

interface EventsProcessor {

    companion object {
        val VersionedEventNameRegexp = "(.*)(\\d{4}(-|_)?\\d{2}(-|_)?\\d{2})".toRegex()
    }

    val eventsFilter: EventsFilter
    val ignoreFailedRequests: Boolean

    fun <T> process(processor: (CloudTrailEvent) -> T): Sequence<T> = scrollEvents().filter { cloudTrailEvent ->
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
            throw RuntimeException("Error while processing event $cloudTrailEvent", t)
        }
    }

    fun parseEvent(jsonMap: Map<String, Any>, eventSourceUri: String? = null): CloudTrailEvent? {
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
            eventSourceUri  = eventSourceUri,
            eventTime       = eventTime,
            eventSource     = jsonMap["eventSource"] as String,
            eventName       = fixedEventName,
            apiVersion      = fixedApiVersion,
            request         = (jsonMap["requestParameters"] as? RequestMap).orEmpty()
        ))
    }

    fun scrollEvents(): Sequence<CloudTrailEvent>

}
