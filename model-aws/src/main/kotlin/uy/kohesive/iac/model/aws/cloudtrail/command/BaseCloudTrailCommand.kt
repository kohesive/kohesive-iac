package uy.kohesive.iac.model.aws.cloudtrail.command

import com.amazonaws.services.cloudtrail.AWSCloudTrailClientBuilder
import io.airlift.airline.Option
import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailAPIEventsProcessor
import uy.kohesive.iac.model.aws.cloudtrail.EventsFilter
import uy.kohesive.iac.model.aws.cloudtrail.EventsProcessor
import uy.kohesive.iac.model.aws.cloudtrail.FileSystemEventsProcessor
import uy.kohesive.iac.model.aws.cloudtrail.utils.DateTime
import java.io.File

abstract class BaseCloudTrailCommand {

    @Option(name = arrayOf("--startTime"), description = "Event date range start")
    val startTime: String? = null

    @Option(name = arrayOf("--endTime"), description = "Event date range end")
    val endTime: String? = null

    @Option(name = arrayOf("--startId"), description = "Event ID range start")
    val startId: String? = null

    @Option(name = arrayOf("--endId"), description = "Event ID range end")
    val endId: String? = null

    @Option(name = arrayOf("--local"), description = "Local filesystem mode")
    var local = false

    @Option(name = arrayOf("--events-dir"), description = "Events directory path")
    val eventsDir: String? = null

    protected fun createEventsProcessor(): EventsProcessor {
        val eventsFilter = getFilter()

        if (local) {
            if (eventsDir == null) {
                throw IllegalStateException("--events-dir parameter is expected to be non-empty for local mode")
            }
            return FileSystemEventsProcessor(
                eventsDir       = File(eventsDir),
                oneEventPerFile = false,
                gzipped         = true,
                eventsFilter    = eventsFilter
           )
        } else {
            // TODO: allow configuring the client (profile, etc)
            val awsClient = AWSCloudTrailClientBuilder.defaultClient()

            return CloudTrailAPIEventsProcessor(
                awsClient    = awsClient,
                eventsFilter = eventsFilter
            )
        }
    }

    protected fun getFilter() = EventsFilter(
        startId   = startId,
        endId     = endId,
        startTime = startTime?.let { DateTime.parseDate(it) },
        endTime   = endTime?.let { DateTime.parseDate(it) }
    )

}