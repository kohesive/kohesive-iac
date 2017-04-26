package uy.kohesive.iac.model.aws.cloudtrail.command

import io.airlift.airline.Option
import uy.kohesive.iac.model.aws.cloudtrail.EventsFilter
import uy.kohesive.iac.model.aws.cloudtrail.utils.DateTime

abstract class BaseCloudTrailCommand {

    @Option(name = arrayOf("--startTime"), description = "Event date range start")
    val startTime: String? = null

    @Option(name = arrayOf("--endTime"), description = "Event date range end")
    val endTime: String? = null

    @Option(name = arrayOf("--startId"), description = "Event ID range start")
    val startId: String? = null

    @Option(name = arrayOf("--endId"), description = "Event ID range end")
    val endId: String? = null

    protected fun getFilter() = EventsFilter(
        startId   = startId,
        endId     = endId,
        startTime = startTime?.let { DateTime.parseDate(it) },
        endTime   = endTime?.let { DateTime.parseDate(it) }
    )

}