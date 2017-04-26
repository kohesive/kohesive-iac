package uy.kohesive.iac.model.aws.cloudtrail.command

import io.airlift.airline.Command

@Command(name = "list-events", description = "Lists CloudTrail events from CloudTrail API or local filesystem")
class ListEventsCommand : BaseCloudTrailCommand(), Runnable {

    override fun run() {
        // TODO: implement
    }

}