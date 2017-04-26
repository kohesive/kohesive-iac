package uy.kohesive.iac.model.aws.cloudtrail.command

import io.airlift.airline.Command

@Command(name = "codegen", description = "Generates AWS API calls code from CloudTrail events fetched via API or stored on a local filesystem")
class CodegenEventsCommand : BaseCloudTrailCommand(), Runnable {

    override fun run() {
        // TODO: implement
    }
}