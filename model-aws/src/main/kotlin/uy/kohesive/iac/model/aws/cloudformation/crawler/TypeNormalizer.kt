package uy.kohesive.iac.model.aws.cloudformation.crawler

object TypeNormalizer {

    private val BuiltInConversions = mapOf(
        "Ref ID"                        to "String",
        "Time stamp"                    to "Number",
        "List of users"                 to "List<String>",
        "List of numbers"               to "List<Number>",
        "List of strings"               to "List<String>",
        "String list"                   to "List<String>",
        "String to string map"          to "Map<String, String>",
        "String to list-of-strings map" to "Map<String, List<String>>",
        "Mapping of key-value pairs"    to "Map<String, String>",
        "List<key-value pairs>"         to "Map<String, String>",
        "an empty map: {}"              to "Map<String, String>",
        "String-to-string map"          to "Map<String, String>",
        "JSON object"                   to "JsonObject"
    )

    private val PrimitiveTypes = setOf(
        "String",
        "Boolean",
        "Number",
        "JsonObject"
    )

    val BuiltinConversionTargets = BuiltInConversions.values.toSet()

    private val listPrefixes = listOf("A list of ", "List of ", "list of ", "Array of ")

    fun normalizeType(propertyType: String?): String? {
        if (propertyType == null) {
            return null
        }

        var normalizedValue = BuiltInConversions.getOrDefault(propertyType, propertyType).trim().replace("-", "")

        if (normalizedValue.startsWith("A JSON")) {
            normalizedValue = "JsonObject"
        }

        if (normalizedValue.startsWith("Keyvalue pairs", ignoreCase = true)) {
            normalizedValue = "Map<String, String>"
        }

        if (normalizedValue.startsWith("String ")) {
            normalizedValue = "String"
        } else if (normalizedValue.startsWith("Number") || normalizedValue.startsWith("Integer") || normalizedValue.startsWith("Double")) {
            normalizedValue = "Number"
        } else {
            listPrefixes.firstOrNull { listPrefix ->
                normalizedValue.startsWith(listPrefix, ignoreCase = true)
            }?.let { listPrefix ->
                var listParameter = normalizedValue.drop(listPrefix.length)
                if (!listParameter.startsWith("AWS::") && !PrimitiveTypes.contains(listParameter)) {
                    listParameter = "String"
                }
                normalizedValue = "List<$listParameter>"
            }
        }

        if (normalizedValue.endsWith("Tags")) {
            normalizedValue = normalizedValue.dropLast(1)
        }
        if (normalizedValue.endsWith("Region")) {
            println(normalizedValue)
        }

        return normalizedValue
    }

}