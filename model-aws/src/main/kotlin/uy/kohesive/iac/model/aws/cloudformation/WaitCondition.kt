package uy.kohesive.iac.model.aws.cloudformation

import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.cloudformation.wait.*
import uy.kohesive.iac.model.aws.utils.DslScope

interface WaitConditionIdentifiable : KohesiveIdentifiable {

}

interface WaitConditionEnabled : WaitConditionIdentifiable {
    val WaitConditionClient: DeferredWaitConditionClient
    val WaitConditionContext: WaitConditionContext
    fun <T> withWaitConditionContext(init: WaitConditionContext.(DeferredWaitConditionClient) -> T): T = WaitConditionContext.init(WaitConditionClient)
}

@DslScope
class WaitConditionContext(private val context: CloudFormationContext) : WaitConditionEnabled by context {

    fun createWaitHandle(name: String): WaitConditionHandle {
        return WaitConditionClient.createWaitConditionHandle(CreateWaitHandleRequest().apply {
            withName(name)
        }).handle
    }

    fun createWaitCondition(name: String, init: CreateWaitConditionRequest.() -> Unit): WaitCondition {
        return WaitConditionClient.createWaitCondition(CreateWaitConditionRequest().apply {
            withName(name)
            init()
        }).waitCondition
    }

}