package uy.kohesive.iac.model.aws

import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import uy.kohesive.iac.model.aws.contexts.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

// @DslScope
open class IacContext(
        val environment: String,
        val planId: String,
        val namingStrategy: IacContextNamingStrategy = IacSimpleEnvPrefixNamingStrategy(),
        init: IacContext.() -> Unit = {}
) : KohesiveIdentifiable, Ec2Enabled, IamRoleEnabled, AutoScalingEnabled {
    override val objectsToIds = IdentityHashMap<Any, String>()

    val variables: MutableMap<String, ParameterizedValue> = hashMapOf()
    val mappings: MutableMap<String, MappedValues> = hashMapOf()

    override val ec2Client: AmazonEC2 by lazy { DeferredAmazonEC2(this) }
    override val ec2Context: Ec2Context by lazy { Ec2Context(this) }
    override val iamClient: AmazonIdentityManagement by lazy { DeferredAmazonIdentityManagement(this) }
    override val iamContext: IamContext by lazy { IamContext(this) }
    override val autoScalingClient: AmazonAutoScaling by lazy { DeferredAmazonAutoScaling(this) }
    override val autoScalingContext: AutoScalingContext by lazy { AutoScalingContext(this) }

    val intVariablesReferences = ConcurrentHashMap<Int, ParameterizedValue>()
    val intVariablesRevReferences = ConcurrentHashMap<ParameterizedValue, Int>()
    val intVariableLastUsed = AtomicInteger(Int.MIN_VALUE)

    fun addOrGetIntVariableRef(variable: ParameterizedValue): Int {
        val currentValue = intVariablesRevReferences.computeIfAbsent(variable) {
            intVariableLastUsed.incrementAndGet()
        }
        intVariablesReferences.putIfAbsent(currentValue, variable)
        return currentValue
    }

    init {
        init()
    }

    fun addVariables(vararg vari: ParameterizedValue) {
        variables.putAll(vari.map { it.name to it })
    }

    fun addMappings(vararg maps: MappedValues) {
        mappings.putAll(maps.map { it.name to it })
    }

    fun build(builder: IacContext.() -> Unit) {
        this.builder()
    }

    fun intValueToStringRef(value: Int): String? = intVariablesReferences.get(value)?.asString()
    fun ParameterizedValue.asString(): String = "{{kohesive:var:$name}}"
    fun ParameterizedValue.asInt(): Int = addOrGetIntVariableRef(this)
}
