package uy.kohesive.iac.model.aws.cloudtrail

import com.google.common.io.Files

class CloudTrailAPIEventsProcessor(ignoreFailedRequests: Boolean) : FileSystemEventsProcessor(
    eventsDir            = Files.createTempDir(),
    oneEventPerFile      = true,
    gzipped              = false,
    ignoreFailedRequests = ignoreFailedRequests
) {

    init {
        EventsFetcher(eventsDir).fetchEvents()
    }

}