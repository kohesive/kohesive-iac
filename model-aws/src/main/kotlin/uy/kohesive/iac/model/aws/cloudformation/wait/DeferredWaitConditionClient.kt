package uy.kohesive.iac.model.aws.cloudformation.wait

import uy.kohesive.iac.model.aws.cloudformation.CloudFormationContext
import uy.kohesive.iac.model.aws.proxy.createReference
import uy.kohesive.iac.model.aws.proxy.makeProxy

class DeferredWaitConditionClient(val context: CloudFormationContext) {

    fun createWaitConditionHandle(request: CreateWaitHandleRequest): CreateWaitHandleResult {
        return with (context) {
            request.registerWithAutoName()
            CreateWaitHandleResult().withHandle(
                WaitConditionHandle().withRef(
                    createReference<WaitConditionHandle>(getNameStrict(request))
                ).registerWithSameNameAs(request)
            ).registerWithSameNameAs(request)
        }
    }

    fun createWaitCondition(request: CreateWaitConditionRequest): CreateWaitConditionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateWaitConditionResult().withWaitCondition(
                makeProxy<CreateWaitConditionRequest, WaitCondition>(context, getNameStrict(request), request, mapOf(
                    CreateWaitConditionRequest::getCount   to WaitCondition::getCount,
                    CreateWaitConditionRequest::getName    to WaitCondition::getName,
                    CreateWaitConditionRequest::getHandle  to WaitCondition::getHandle,
                    CreateWaitConditionRequest::getTimeout to WaitCondition::getTimeout
                )).registerWithSameNameAs(request)
            ).registerWithSameNameAs(request)
        }
    }

}

