package ${contextPackageName}

import uy.kohesive.iac.model.aws.clients.*
import uy.kohesive.iac.model.aws.contexts.*
<#list unversionedClients as client>
import ${client.awsInterfaceClassFq}
</#list>

// @DslScope
open class IacContext(
    val environment: String,
    val planId: String,
    val namingStrategy: IacContextNamingStrategy = IacSimpleEnvPrefixNamingStrategy(),
    init: IacContext.() -> Unit = {}
): BaseIacContext(), <#list contexts as x>${x.enabledClassName}<#if x_has_next>, </#if></#list> {

    // Clients
    <#list clients as client>
    override val ${client.clientFieldName}: <#if client.versioned>${client.awsInterfaceClassFq}<#else>${client.awsInterfaceClassName}</#if> by lazy { ${client.deferredClientClassName}(this) }
    </#list>

    // Contexts
    <#list contexts as context>
    override val ${context.contextFieldName}: ${context.contextClassName} by lazy { ${context.contextClassName}(this) }
    </#list>

    init {
        init()
    }

}