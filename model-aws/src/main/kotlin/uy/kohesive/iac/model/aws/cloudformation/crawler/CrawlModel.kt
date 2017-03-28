package uy.kohesive.iac.model.aws.cloudformation.crawler

import uy.kohesive.iac.model.aws.utils.firstLetterToUpperCase

data class CrawlTask(
    val resourceType: String,
    val uri: String
)

data class CloudFormationResource(
    val sourceURL: String,
    val resourceType: String,
    val properties: List<ResourceProperty>
) {
    override fun toString() = resourceType + "\n  " + properties.joinToString("\n  ")
}

data class ResourceProperty(
    val propertyName: String,
    val propertyType: String,
    val required: Boolean,
    val propertyHref: String? = null
) {

    override fun toString() = "${if (required) "+" else "-"} $propertyName: $propertyType"
}

data class PropertyNameExtractor(
    val prefix: String,
    val stopWords: List<String>
) {
    fun extract(text: String): String? = text.takeIf { text.startsWith(prefix + " ") }?.drop(prefix.length + 1)?.let { prefixLess ->
        stopWords.map { prefixLess.indexOf(" " + it) }.filter { it > 0 }.min()?.let { indexEnd ->
            prefixLess.substring(0, indexEnd).split(' ').map(String::firstLetterToUpperCase).joinToString("").trim()
        }
    }
}