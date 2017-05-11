package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.stepfunctions.model.CreateActivityRequest
import com.amazonaws.services.stepfunctions.model.CreateStateMachineRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.StepFunctions

class StepFunctionsActivityResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateActivityRequest> {

    override val requestClazz = CreateActivityRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateActivityRequest).let {
            StepFunctions.Activity(
                Name = request.name
            )
        }

}

class StepFunctionsStateMachineResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateStateMachineRequest> {

    override val requestClazz = CreateStateMachineRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateStateMachineRequest).let {
            StepFunctions.StateMachine(
                RoleArn          = request.roleArn,
                DefinitionString = request.definition
            )
        }

}

