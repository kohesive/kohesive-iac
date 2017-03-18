package uy.kohesive.iac.model.aws

import java.util.*

abstract class BaseIacContext : KohesiveIdentifiable {

    override val objectsToNames = IdentityHashMap<Any, String>()

    val dependsOn: MutableMap<Any, MutableList<Any>> = mutableMapOf()
    val variables: MutableMap<String, ParameterizedValue<out Any>> = mutableMapOf()
    val mappings: MutableMap<String, MappedValues> = mutableMapOf()

    val numericVarTracker = NumericVariableTracker()

    fun addVariables(vararg vari: ParameterizedValue<out Any>) {
        variables.putAll(vari.map { it.name to it })
    }

    fun addMappings(vararg maps: MappedValues) {
        mappings.putAll(maps.map { it.name to it })
    }

    fun Any.makeDependable(on: Any) {
        (dependsOn.getOrPut(this) { ArrayList<Any>() }).add(on)
    }

    val ParameterizedValue<String>.value: String get() = "{{kohesive:var:$name}}"
    val ParameterizedValue<Int>.value: Int get() = numericVarTracker.addOrGetNumericVariableRef(this)
    val ParameterizedValue<Long>.value: Long get() = numericVarTracker.addOrGetNumericVariableRef(this).toLong()

}