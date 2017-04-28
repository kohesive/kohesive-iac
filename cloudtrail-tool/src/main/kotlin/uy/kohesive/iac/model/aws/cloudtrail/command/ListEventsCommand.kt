package uy.kohesive.iac.model.aws.cloudtrail.command

import io.airlift.airline.Command
import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailEvent

@Command(name = "list-events", description = "Lists CloudTrail events from CloudTrail API or local filesystem")
class ListEventsCommand : BaseCloudTrailCommand(), Runnable {

    override fun run() {
        println("AWS CloudTrailEvents matching the input filter ${getFilter()}\n")
        createEventsProcessor().process(CloudTrailEvent::toString).forEach(::println)
    }

}