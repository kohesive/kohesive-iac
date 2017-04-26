package uy.kohesive.iac.model.aws.cloudtrail.command

import io.airlift.airline.Cli
import io.airlift.airline.Help

object CloudTrailTool {

    @JvmStatic fun main(vararg args: String) {
        val builder = Cli.builder<Runnable>("cloud-trail-tool")
            .withDescription("Kohesive CloudTrail Tool")
            .withDefaultCommand(Help::class.java)
            .withCommand(Help::class.java)
            .withCommands(
                ListEventsCommand::class.java,
                CodegenEventsCommand::class.java
            )
        builder.build().parse(args.toList()).run()
    }

}