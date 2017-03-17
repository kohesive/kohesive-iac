package uy.kohesive.iac.model.aws

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class NumericVariableTracker {
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