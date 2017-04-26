package uy.kohesive.iac.model.aws.cloudtrail

import java.util.*

interface EventsProcessor {

    fun <T> process(processor: (CloudTrailEvent) -> T): Sequence<T>

}

data class EventsFilter(
    val startTime: Date? = null,
    val endTime: Date? = null
) {
    companion object {
        val Empty = EventsFilter(
            startTime = null,
            endTime = null
        )
    }

    fun isEmpty() = this == Empty

}