package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.services.cloudtrail.AWSCloudTrail
import com.amazonaws.services.cloudtrail.AWSCloudTrailClientBuilder
import com.amazonaws.services.cloudtrail.model.Event
import com.amazonaws.services.cloudtrail.model.LookupEventsRequest
import java.io.File

class EventsFetcher(val outputDir: File) {

    fun fetchEvents() {
        AWSCloudTrailClientBuilder.defaultClient().eventsSequence().forEach { event ->
            File(outputDir, event.eventId + ".json").writeText(event.cloudTrailEvent)
        }
    }

}

fun AWSCloudTrail.eventsSequence(): Sequence<Event> = CloudTrailEventsSequence(this)

private class CloudTrailEventsSequence(private val cloudTrailClient: AWSCloudTrail) : Sequence<Event> {

    override fun iterator(): Iterator<Event> {
        val firstLookup = cloudTrailClient.lookupEvents()
        val firstNextToken: String? = firstLookup.nextToken

        return CloudTrailEventsIterator(
            currentBunchIterator = firstLookup.events.iterator(),
            nextToken            = firstNextToken,
            cloudTrailClient     = cloudTrailClient
        )
    }

}

private class CloudTrailEventsIterator(
    private var currentBunchIterator: Iterator<Event>,
    private var nextToken: String?,
    private val cloudTrailClient: AWSCloudTrail
) : Iterator<Event> {

    override fun hasNext(): Boolean {
        return currentBunchIterator.hasNext() || nextToken != null
    }

    override fun next(): Event {
        if (currentBunchIterator.hasNext()) {
            return currentBunchIterator.next()
        } else {
            if (nextToken != null) {
                val lookupEventsResult = cloudTrailClient.lookupEvents(LookupEventsRequest().withNextToken(nextToken))

                nextToken = lookupEventsResult.nextToken
                currentBunchIterator = lookupEventsResult.events.iterator()

                return currentBunchIterator.next()
            } else {
                throw IllegalStateException("Next token supposed to be non-empty at this point")
            }
        }
    }

}