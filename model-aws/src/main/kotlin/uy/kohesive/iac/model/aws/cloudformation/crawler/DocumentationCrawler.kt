package uy.kohesive.iac.model.aws.cloudformation.crawler

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.TextNode
import uy.klutter.core.jdk.mustNotEndWith
import uy.klutter.core.jdk.mustNotStartWith
import uy.kohesive.iac.model.aws.utils.firstLetterToUpperCase
import uy.kohesive.iac.model.aws.utils.singularize
import java.io.File
import java.net.SocketTimeoutException
import java.net.URL

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
    val isRequired: Boolean,
    val propertyHref: String? = null
) {

    override fun toString() = "${if (isRequired) "+" else "-"} $propertyName: $propertyType"
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

class DocumentationCrawler(
    val baseUri: String = DocumentationCrawler.CFDocsURL,
    val localMode: Boolean = false,
    val downloadFiles: Boolean = false
) {

    companion object {
        val CFDocsURL = "http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/"

        val ResourcesThatAreActuallyProperties = setOf(
            "aws-resource-codepipeline-customactiontype-artifactdetails.html",
            "aws-resource-codepipeline-customactiontype-settings.html",
            "aws-resource-route53-hostedzone-hostedzonevpcs.html"
        )

        val AWSDocsAccessThrottlingInterval = 500L

        val PropertyNameExtractors = listOf(
            PropertyNameExtractor("Describes the", listOf("of", "for")),
            PropertyNameExtractor("Describes a", listOf("of", "for")),
            PropertyNameExtractor("Describes", listOf("of", "for")),
            PropertyNameExtractor("The", listOf("property")),
            PropertyNameExtractor("A list of", listOf("for"))
        )

        private fun extractPropertyName(text: String): String? = PropertyNameExtractors
            .asSequence().map { it.extract(text) }.firstOrNull { it != null }

    }

    private val crawledResources = linkedMapOf<String, CloudFormationResource>()
    private val hrefToTypeName = hashMapOf<String, String>()

    fun getJsoupDocument(uri: String) = if (localMode) {
        if (downloadFiles) {
            val targetFile = File(baseUri, uri)
            if (!targetFile.exists()) {
                Thread.sleep(AWSDocsAccessThrottlingInterval)
                targetFile.writeText((URL(CFDocsURL + uri).readText()))
            }
        }
        getJsoupDocumentFromFile(baseUri + uri)
    } else {
        getJsoupDocumentForUrl(baseUri + uri)
    }

    fun crawl(): List<CloudFormationResource> {
        val resources = doCrawl()

        // Validate
        resources.filter { it.properties.isEmpty() }.forEach {
            if (!it.resourceType.startsWith("AWS::CloudFormation")) {
                throw IllegalStateException("${it.resourceType} properties list is empty")
            }
        }

        return resources
    }

    private fun doCrawl(): List<CloudFormationResource> {
        val resourcesListDoc = getJsoupDocument("aws-template-resource-type-ref.html")
        val resourceUris     = resourcesListDoc.select(".highlights li a").map {
            CrawlTask(
                resourceType = it.text(),
                uri          = it.attr("href")
            )
        }

        resourceUris.forEach { (resourceType, uri) ->
            hrefToTypeName.put(uri, resourceType)
        }
        resourceUris.forEach { (_, uri) ->
            try {
                crawlResource(uri)
            } catch (t: Throwable) {
                throw RuntimeException("Error while processing $uri", t)
            }
        }

        return crawledResources.values.toList()
    }

    // How smart is this, huh?
    private fun figureOutPropertyTypeFromHrefAndHeader(uri: String, header: String?): String? {
        if (header == null) {
            return null
        }

        val lastUrlWord = uri.dropLast(".html".length).split('-').last()
        val splitHeader = header.split(' ')
        var subHeader = ""
        for (i in splitHeader.indices.reversed()) {
            subHeader = splitHeader[i] + subHeader

            if (subHeader.contains(lastUrlWord, ignoreCase = true)) {
                return subHeader
            }
        }

        return null
    }

    fun crawlResourceType(uri: String): String {
        try {
            val alreadyCrawledTypeName = hrefToTypeName[uri]
            if (alreadyCrawledTypeName != null) {
                return alreadyCrawledTypeName
            }

            val resourceDoc = getJsoupDocument(uri)

            // Let's figure out our resource type
            return if (uri.contains("properties") || uri.contains("property") || ResourcesThatAreActuallyProperties.contains(uri)) {
                if (uri == "aws-properties-resource-tags.html") {
                    "AWS::CloudFormation::ResourceTag" // reserved case
                } else {
                    val header = resourceDoc.select("h1.topictitle")?.firstOrNull()?.text()

                    // Let's check the hrefToTypeName first
                    hrefToTypeName[uri] ?: resourceDoc.select("div#main-col-body p").firstOrNull()?.let { p ->
                        var propertyName = figureOutPropertyTypeFromHrefAndHeader(uri, header) ?:
                            extractPropertyName(p.text()) ?:
                            p.select("code").firstOrNull()?.text()

                        var parentHref = p.select("a").firstOrNull()?.attr("href")?.sanitizeLink()

                        // Bugs in CF documents
                        if (uri == "aws-properties-dynamodb-projectionobject.html") {
                            parentHref = "aws-resource-dynamodb-table.html"
                        } else if (uri == "aws-properties-iot-actions.html") {
                            parentHref = "aws-properties-iot-topicrulepayload.html"
                        } else if (parentHref == null && uri.startsWith("aws-properties-iot") && p.text().contains("is a property of the Actions property")) {
                            parentHref = "aws-properties-iot-actions.html"
                        }

                        propertyName = propertyName
                            ?.mustNotEndWith("PropertyType")
                            ?.mustNotEndWith("Property")
                            ?.mustNotEndWith("Type")
                            ?.mustNotEndWith("Object")
                            ?.singularize()

                        // Validate
                        if (uri == parentHref) {
                            // Known bug in CF documentation, when a link refs the same page instead of its parent
                            // Let's fix it by removing the last segment:
                            parentHref = uri.dropLast(".html".length).split('-').dropLast(1).joinToString("-") + ".html"
                        }
                        if (propertyName == null || parentHref == null) {
                            throw IllegalStateException("Can't figure out property object name for $uri")
                        }
                        val fqName = hrefToTypeName[parentHref]?.let { parentName ->
                            parentName + "::" + propertyName
                        } ?: (
                            crawlResourceType(parentHref) + "::" + propertyName
                        )
                        hrefToTypeName[uri] = fqName
                        fqName
                    } ?: throw IllegalStateException("Can't figure out property object name for $uri")
                }
            } else {
                hrefToTypeName[uri] ?: throw IllegalStateException("Can't figure object name for $uri")
            }
        } catch (t: Throwable) {
            throw RuntimeException("Error while crawling $uri resource name", t)
        }
    }

    fun crawlResource(uri: String): CloudFormationResource {
        // Check for already crawled first
        val alreadyCrawled = crawledResources[hrefToTypeName[uri]]
        if (alreadyCrawled != null) {
            return alreadyCrawled
        }

        val resourceDoc  = getJsoupDocument(uri)
        val deferredUris = ArrayList<String>()

        val resourceProperties = resourceDoc.select(".variablelist").flatMap { varListDiv ->
            // We go back to find the first h2 tag with a listing type (Syntax, Properties, etc)
            var previousElementSibling = varListDiv.previousElementSibling()
            var variablesType: String? = null
            while (true) {
                if (previousElementSibling.tagName() == "h2") {
                    variablesType = previousElementSibling.text()
                    break
                }
                previousElementSibling = previousElementSibling.previousElementSibling() ?: break
            }

            // Let's check if we're in a right listing
            if (setOf("Properties", "Parameters", "Members").contains(variablesType)) {
                varListDiv.select("dl dt").filter {
                    // To avoid nested .variablelist parsing
                    it.parent().parent() == varListDiv
                }.map { propertyDt ->
                    val propertyName = propertyDt.text().trim()

                    var propertyType: String? = null
                    var isRequired: Boolean?  = null
                    var typeHref: String?     = null

                    // Alright, we're in, let's parse things
                    val propertyDd = propertyDt.nextElementSibling()
                    propertyDd.select("em").forEach { em ->
                        (em.nextSibling() as? TextNode)?.text()?.trim()?.mustNotStartWith("::")?.mustNotStartWith(":")?.mustNotEndWith('.')?.trim()?.let { value ->
                            if (em.text() == "Type" || em.text() == "Type:") {
                                typeHref = em.parent().select("a").firstOrNull()?.attr("href")
                                val targetTypeName = typeHref?.sanitizeLink()?.let { sanitizedUri ->
                                    if (uri != sanitizedUri) { // Prevent self-linking
                                        deferredUris.add(sanitizedUri)
                                        crawlResourceType(sanitizedUri)
                                    } else {
                                        hrefToTypeName[uri]
                                    }
                                }

                                propertyType = if (targetTypeName == null) {
                                    value
                                } else {
                                    ((if (value.isEmpty()) value else value + " ") + targetTypeName).trim()
                                }
                            } else if (em.text() == "Required") {
                                isRequired = "Yes" == value.trim()
                            }
                        }
                    }

                    // Bugs in documentation
                    if (propertyName == "PolicyName" && uri == "aws-properties-ec2-elb-LBCookieStickinessPolicy.html") {
                        propertyType = "String"
                        isRequired   = true
                    }
                    if (propertyName == "RetryOptions" && uri == "aws-properties-kinesisfirehose-kinesisdeliverystream-elasticsearchdestinationconfiguration.html") {
                        isRequired = true
                    }
                    if (propertyName == "S3Configuration" && uri == "aws-properties-kinesisfirehose-kinesisdeliverystream-elasticsearchdestinationconfiguration.html") {
                        isRequired = true
                    }
                    if (propertyName == "CIDRIP" && uri == "aws-resource-rds-security-group-ingress.html") {
                        isRequired = true
                    }
                    if (propertyName == "Region" && uri == "aws-properties-route53-recordset.html") {
                        isRequired = false
                    }

                    // Validate
                    if (propertyType == null) {
                        // Try syntax "JSON" (not a JSON really) parsing for property type
                        val programListing = resourceDoc.select(".programlisting")
                        propertyType = programListing.filter {
                            (it.parent().id() == "JSON" || it.previousElementSibling()?.text() == "JSON") && it.parent().toString().contains("syntax", ignoreCase = true)
                        }.map {
                            it.select("code").firstOrNull()?.text()
                        }.firstOrNull()?.let { syntaxJson ->
                            CloudFormationExampleSyntaxParser.parse(syntaxJson)[propertyName]
                        }

                        if (propertyType == null) {
                            throw IllegalStateException("Can't parse property $propertyName type in $uri")
                        }
                    }
                    if (isRequired == null) {
                        throw IllegalStateException("Can't parse whether $propertyName is required in $uri")
                    }

                    // Normalize type
                    propertyType = TypeNormalizer.normalizeType(propertyType!!)

                    ResourceProperty(
                        propertyName = propertyName,
                        propertyType = propertyType!!,
                        isRequired   = isRequired!!,
                        propertyHref = typeHref
                    )
                }
            } else {
                emptyList()
            }
        }

        return CloudFormationResource(
            sourceURL    = CFDocsURL + uri,
            resourceType = crawlResourceType(uri),
            properties   = resourceProperties
        ).apply {
            crawledResources[resourceType] = this@apply
            deferredUris.forEach { crawlResource(it) }
        }
    }

    private fun String.sanitizeLink(): String = this.replace(CFDocsURL, "").let {
        if (it.contains('#')) {
            it.substring(0, it.indexOf('#'))
        } else {
            it
        }
    }

}

private fun getJsoupDocumentFromFile(filePath: String): Document {
    return Jsoup.parse(File(filePath), Charsets.UTF_8.toString())
}

private fun getJsoupDocumentForUrl(url: String): Document {
    try {
        val document = Jsoup.connect(url).timeout(10000).get()
        return document
    } catch (ex: SocketTimeoutException) {
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
        return getJsoupDocumentForUrl(url)
    } catch (ex: Exception) {
        throw ex
    }
}
