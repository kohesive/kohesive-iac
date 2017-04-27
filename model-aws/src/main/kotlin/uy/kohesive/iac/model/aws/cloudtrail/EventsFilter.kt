package uy.kohesive.iac.model.aws.cloudtrail

import java.util.*

data class EventsFilter(
    val startTime: Date?,
    val endTime: Date?,
    val startId: String?,
    val endId: String?
) {

    companion object {
        val Empty = EventsFilter(
            startTime = null,
            endTime   = null,
            startId   = null,
            endId     = null
        )
    }

    fun isEmpty() = this == Empty
    fun hasIdConstraints() = startId != null || endId != null

}