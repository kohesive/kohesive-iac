package ${contextPackageName}

import uy.kohesive.iac.model.aws.clients.*
import uy.kohesive.iac.model.aws.contexts.*
<#list clients as client>
import ${client.awsInterfaceClassFq}
</#list>
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

// @DslScope
open class IacContext(
    val environment: String,
    val planId: String,
    val namingStrategy: IacContextNamingStrategy = IacSimpleEnvPrefixNamingStrategy(),
    init: IacContext.() -> Unit = {}
): KohesiveIdentifiable, <#list contexts as x>${x.enabledClassName}<#if x_has_next>, </#if></#list> {

    override val objectsToNames = IdentityHashMap<Any, String>()

    val dependsOn: MutableMap<Any, MutableList<Any>> = mutableMapOf()
    val variables: MutableMap<String, ParameterizedValue<out Any>> = mutableMapOf()
    val mappings: MutableMap<String, MappedValues> = mutableMapOf()


    // Clients
    <#list clients as client>
    override val ${client.clientFieldName}: ${client.awsInterfaceClassName} by lazy { ${client.deferredClientClassName}(this) }
    </#list>

    // Contexts
    <#list contexts as context>
    override val ${context.contextFieldName}Context: ${context.contextClassName} by lazy { ${context.contextClassName}(this) }
    </#list>

    val numericVarTracker = NumericVariableTracker()

    init {
        init()
    }

    fun addVariables(vararg vari: ParameterizedValue<out Any>) {
        variables.putAll(vari.map { it.name to it })
    }

    fun addMappings(vararg maps: MappedValues) {
        mappings.putAll(maps.map { it.name to it })
    }

    fun build(builder: IacContext.() -> Unit) {
        this.builder()
    }

    fun Any.makeDependable(on: Any) {
        (dependsOn.getOrPut(this) { ArrayList<Any>() }).add(on)
    }

    val ParameterizedValue<String>.value: String get() = "{{kohesive:var:$name}}"
    val ParameterizedValue<Int>.value: Int get() = numericVarTracker.addOrGetNumericVariableRef(this)
    val ParameterizedValue<Long>.value: Long get() = numericVarTracker.addOrGetNumericVariableRef(this).toLong()

}