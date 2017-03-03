package uy.kohesive.iac.model.aws

import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import uy.kohesive.iac.model.aws.contexts.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

// @DslScope
open class IacContext(
        val environment: String,
        val planId: String,
        val namingStrategy: IacContextNamingStrategy = IacSimpleEnvPrefixNamingStrategy(),
        init: IacContext.() -> Unit = {}
) : KohesiveIdentifiable, Ec2Enabled, IamRoleEnabled, AutoScalingEnabled {

    override val objectsToNames= IdentityHashMap<Any, String>()

    val variables: MutableMap<String, ParameterizedValue<out Any>> = hashMapOf()
    val mappings: MutableMap<String, MappedValues> = hashMapOf()

    override val ec2Client: AmazonEC2 by lazy { DeferredAmazonEC2(this) }
    override val ec2Context: Ec2Context by lazy { Ec2Context(this) }
    override val iamClient: AmazonIdentityManagement by lazy { DeferredAmazonIdentityManagement(this) }
    override val iamContext: IamContext by lazy { IamContext(this) }
    override val autoScalingClient: AmazonAutoScaling by lazy { DeferredAmazonAutoScaling(this) }
    override val autoScalingContext: AutoScalingContext by lazy { AutoScalingContext(this) }

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

    val numericVarTracker = NumericVariableTracker()

    val ParameterizedValue<String>.value: String get() = "{{kohesive:var:$name}}"
    val ParameterizedValue<Int>.value: Int get() = numericVarTracker.addOrGetNumericVariableRef(this)
    val ParameterizedValue<Long>.value: Long get() = numericVarTracker.addOrGetNumericVariableRef(this).toLong()

    class NumericVariableTracker() {
        val numericToVarMap = ConcurrentHashMap<Int, ParameterizedValue<out Any>>()
        val varToNumericMap = ConcurrentHashMap<ParameterizedValue<out Any>, Int>()
        val lastUsed = AtomicInteger(Int.MIN_VALUE)

        fun addOrGetNumericVariableRef(variable: ParameterizedValue<out Any>): Int {
            val currentValue = varToNumericMap.computeIfAbsent(variable) {
                lastUsed.incrementAndGet()
            }
            numericToVarMap.putIfAbsent(currentValue, variable)
            return currentValue
        }
    }
}
