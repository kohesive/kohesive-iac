package uy.kohesive.iac.model.aws

class MappedValues(val name: String, val map: Map<String, Map<String, String>>) {

    fun asRef(keyVariable: String, valueVariable: String): String = "{{kohesive:map:$name:$keyVariable:$valueVariable}}"
    fun asLookup(keyVariable: String, valueVariable: String): String = asRef(keyVariable, valueVariable)

    operator fun get(keyVariable: String): PartialKey = PartialKey(keyVariable)

    inner class PartialKey(val keyVariable: String) {
        operator fun get(valueVariable: String) = asLookup(keyVariable, valueVariable)
    }

}


