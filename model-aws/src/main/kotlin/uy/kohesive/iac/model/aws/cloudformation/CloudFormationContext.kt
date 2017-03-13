package uy.kohesive.iac.model.aws.cloudformation

import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.IacContextNamingStrategy
import uy.kohesive.iac.model.aws.IacSimpleEnvPrefixNamingStrategy
import uy.kohesive.iac.model.aws.cloudformation.wait.DeferredWaitConditionClient
import uy.kohesive.iac.model.aws.proxy.KohesiveReference
import uy.kohesive.iac.model.aws.proxy.createReference

class CloudFormationContext(
    environment: String,
    planId: String,
    namingStrategy: IacContextNamingStrategy = IacSimpleEnvPrefixNamingStrategy(),
    init: CloudFormationContext.() -> Unit = {}
) : IacContext(environment, planId, namingStrategy, {}), WaitConditionEnabled {

    val outputs: MutableList<Output> = mutableListOf()

    override val WaitConditionClient: DeferredWaitConditionClient by lazy { DeferredWaitConditionClient(this) }
    override val WaitConditionContext: WaitConditionContext by lazy { uy.kohesive.iac.model.aws.cloudformation.WaitConditionContext(this) }

    init {
        init()
    }

    internal inline fun <reified T : Any> addAsOutput(name: String, output: T, description: String? = null) {
        outputs.add(Output(
            logicalId   = name,
            value       = if (output is String && KohesiveReference.isReference(output)) {
                output
            } else {
                createReference<T>(getNameStrict(output))
            },
            description = description
        ))
    }

    data class Output(
        val logicalId: String,
        val description: String?,
        val value: String
    )

}