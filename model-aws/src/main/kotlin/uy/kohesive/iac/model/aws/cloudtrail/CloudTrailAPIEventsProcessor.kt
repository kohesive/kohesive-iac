package uy.kohesive.iac.model.aws.cloudtrail

import com.google.common.io.Files

class CloudTrailAPIEventsProcessor(ignoreFailedRequests: Boolean, eventsFilter: EventsFilter = EventsFilter.Empty) : FileSystemEventsProcessor(
    eventsFilter         = eventsFilter,
    eventsDir            = Files.createTempDir(),
    oneEventPerFile      = true,
    gzipped              = false,
    ignoreFailedRequests = ignoreFailedRequests
) {

    init {
        EventsFetcher(eventsDir, eventsFilter).fetchEvents()
    }

}