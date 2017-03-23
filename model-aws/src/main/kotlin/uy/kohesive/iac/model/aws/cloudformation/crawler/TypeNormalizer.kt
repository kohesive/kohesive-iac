package uy.kohesive.iac.model.aws.cloudformation.crawler

object TypeNormalizer {

    val BuiltInConversions = mapOf(
        "List of numbers" to "List<Long>",
        "List of strings" to "List<String>",
        "String list" to "List<String>",
        "String to string map" to "Map<String, String>",
        "String to list-of-strings map" to "Map<String, List<String>>"
    )

    private val listPrefixes = listOf("A list of ", "List of ", "list of ")

    fun normalizeType(propertyType: String): String {
        var normalizedValue = BuiltInConversions.getOrDefault(propertyType, propertyType).trim()

        if (normalizedValue.startsWith("String ")) {
            normalizedValue = "String"
        } else if (normalizedValue.startsWith("Number")) {
            normalizedValue = "Long"
        } else {
            listPrefixes.firstOrNull { listPrefix ->
                normalizedValue.startsWith(listPrefix)
            }?.let { listPrefix ->
                normalizedValue = "List<" + (normalizedValue.drop(listPrefix.length)) + ">"
            }
        }

        if (normalizedValue.endsWith("Tags")) {
            normalizedValue = normalizedValue.dropLast(1)
        }
        if (normalizedValue.endsWith("Region")) {
            println(normalizedValue)
        }

        // TODO: work in progress

        return normalizedValue
    }

}