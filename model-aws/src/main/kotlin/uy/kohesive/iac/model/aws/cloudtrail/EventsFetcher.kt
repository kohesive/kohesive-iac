package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.services.cloudtrail.AWSCloudTrail
import com.amazonaws.services.cloudtrail.AWSCloudTrailClientBuilder
import com.amazonaws.services.cloudtrail.model.Event
import com.amazonaws.services.cloudtrail.model.LookupEventsRequest
import java.io.File

class EventsFetcher(val outputDir: File, val eventsFilter: EventsFilter) {

    fun fetchEvents() {
        AWSCloudTrailClientBuilder.defaultClient().eventsSequence(eventsFilter).forEach { event ->
            File(outputDir, event.eventId + ".json").writeText(event.cloudTrailEvent)
        }
    }

}

fun EventsFilter.toRequest() = LookupEventsRequest().apply {
    startTime = this@toRequest.startTime
    endTime   = this@toRequest.endTime
}

fun AWSCloudTrail.eventsSequence(eventsFilter: EventsFilter): Sequence<Event> {
    var sequence: Sequence<Event> = CloudTrailEventsSequence(this, eventsFilter)
    if (eventsFilter.hasIdConstraints()) {
        if (eventsFilter.startId != null) {
            sequence = sequence.dropWhile { it.eventId != eventsFilter.startId }
        }
        if (eventsFilter.endId != null) {
            sequence = sequence.takeWhile { it.eventId != eventsFilter.endId }
        }
        return sequence
    } else {
        return sequence
    }
}

private class CloudTrailEventsSequence(
    private val cloudTrailClient: AWSCloudTrail,
    private val eventsFilter: EventsFilter
) : Sequence<Event> {

    override fun iterator(): Iterator<Event> {
        val firstLookup = cloudTrailClient.lookupEvents(eventsFilter.toRequest())
        val firstNextToken: String? = firstLookup.nextToken

        return CloudTrailEventsIterator(
            currentBunchIterator = firstLookup.events.iterator(),
            nextToken            = firstNextToken,
            cloudTrailClient     = cloudTrailClient,
            eventsFilter         = eventsFilter
        )
    }

}

private class CloudTrailEventsIterator(
    private var currentBunchIterator: Iterator<Event>,
    private var nextToken: String?,
    private val cloudTrailClient: AWSCloudTrail,
    private val eventsFilter: EventsFilter
) : Iterator<Event> {

    override fun hasNext(): Boolean {
        return currentBunchIterator.hasNext() || nextToken != null
    }

    override fun next(): Event {
        if (currentBunchIterator.hasNext()) {
            return currentBunchIterator.next()
        } else {
            if (nextToken != null) {
                val lookupEventsResult = cloudTrailClient.lookupEvents(eventsFilter.toRequest().withNextToken(nextToken))

                nextToken = lookupEventsResult.nextToken
                currentBunchIterator = lookupEventsResult.events.iterator()

                return currentBunchIterator.next()
            } else {
                throw IllegalStateException("Next token supposed to be non-empty at this point")
            }
        }
    }

}