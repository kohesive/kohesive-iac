package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.cloudwatchevents.model.PutRuleRequest
import com.amazonaws.services.cloudwatchevents.model.PutTargetsRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.Events

class EventsRuleResourcePropertiesBuilder : ResourcePropertiesBuilder<PutRuleRequest> {

    override val requestClazz = PutRuleRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as PutRuleRequest).let {
            Events.Rule(
                Description        = request.description,
                EventPattern       = request.eventPattern,
                Name               = request.name,
                RoleArn            = request.roleArn,
                ScheduleExpression = request.scheduleExpression,
                State              = request.state,
                Targets            = relatedObjects.filterIsInstance<PutTargetsRequest>().flatMap { it.targets }.map {
                    Events.Rule.TargetProperty(
                        Arn       = it.arn,
                        Id        = it.id,
                        Input     = it.input,
                        InputPath = it.inputPath
                    )
                }
            )
        }

}

