package uy.kohesive.iac.model.aws.cloudformation.crawler

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.TextNode
import uy.klutter.core.jdk.mustNotEndWith
import uy.klutter.core.jdk.mustNotStartWith
import java.io.File
import java.net.SocketTimeoutException
import java.net.URL

fun main(args: Array<String>) {
    Crawler(
        baseUri   = "/Users/eliseyev/TMP/cf/",
        localMode = true,
        downloadFiles = true
    ).crawl().forEach(::println)
}

data class ResourceUri(
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

class Crawler(
    val baseUri: String = Crawler.BaseURL,
    val localMode: Boolean = false,
    val downloadFiles: Boolean = false
) {

    companion object {
        val BaseURL = "http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/"
    }

    private val crawledResources = linkedMapOf<String, CloudFormationResource>()

    fun getJsoupDocument(uri: String) = if (localMode) {
        if (downloadFiles) {
            val targetFile = File(baseUri, uri)
            if (!targetFile.exists()) {
                Thread.sleep(500)
                targetFile.writeText((URL(BaseURL + uri).readText()))
                println("Done writing $uri")
            }
        }
        getJsoupDocumentFromFile(baseUri + uri)
    } else {
        getJsoupDocumentForUrl(baseUri + uri)
    }

    fun crawl(): List<CloudFormationResource> {
        val resourcesListDoc = getJsoupDocument("aws-template-resource-type-ref.html")

        val resourceUris = resourcesListDoc.select(".highlights li a").map {
            ResourceUri(
                resourceType = it.text(),
                uri          = it.attr("href")
            )
        }

        // TODO: delete take
        resourceUris.forEach { (resourceType, uri) ->
            try {
                crawlResourceType(resourceType, uri)
            } catch (t: Throwable) {
                throw RuntimeException("Error while processing $uri", t)
            }
        }

        return crawledResources.values.toList()
    }

    data class CrawlTask(
        val resourceType: String,
        val uri: String
    )

    private fun crawlResourceType(resourceType: String, uri: String): CloudFormationResource {
        // Check for already crawled first
        val alreadyCrawled = crawledResources[resourceType]
        if (alreadyCrawled != null) {
            return alreadyCrawled
        }

        val deferredTasks = ArrayList<CrawlTask>()

        val resourceDoc = getJsoupDocument(uri)
        val resourceProperties = resourceDoc.select(".variablelist").flatMap { varListDiv ->
            val previousElementSibling = varListDiv.previousElementSibling()
            val variablesType = previousElementSibling.text()

            if (setOf("Properties", "Parameters", "Members").contains(variablesType)) {
                varListDiv.select("dl dt").filter {
                    // To avoid nested .variablelist parsing
                    it.parent().parent() == varListDiv
                }.map { propertyDt ->
                    val propertyName = propertyDt.text().trim()

                    var propertyType: String? = null
                    var isRequired: Boolean?  = null
                    var typeHref: String?     = null

                    val propertyDd = propertyDt.nextElementSibling()
                    propertyDd.select("em").forEach { em ->
                        (em.nextSibling() as? TextNode)?.text()?.trim()?.mustNotStartWith("::")?.mustNotStartWith(":")?.mustNotEndWith('.')?.trim()?.let { value ->
                            if (em.text() == "Type") {
                                var referencedObject = em.parent().select("a").firstOrNull()?.let { link ->
                                    typeHref = link.attr("href")

                                    if (typeHref == uri) {
                                        resourceType // self-ref
                                    } else {
                                        link.text().split(' ').lastOrNull()?.let {
                                            if (typeHref?.contains("properties") ?: false) {
                                                resourceType + "::" + it
                                            } else {
                                                it
                                            }
                                        }
                                    }
                                } ?: ""

                                // Crawl the sub-property
                                if (referencedObject.isNotEmpty() && typeHref != null) {
                                    if (typeHref == "aws-properties-dynamodb-gsi.html") {
                                        referencedObject = referencedObject.replace("Indexes", "GlobalIndex")
                                    } else if (typeHref == "aws-properties-dynamodb-lsi.html") {
                                        referencedObject = referencedObject.replace("Indexes", "LocalIndex")
                                    }

                                    // Thanks go-cloudformation
                                    if (propertyName == "Icmp" && typeHref == "aws-properties-ec2-networkaclentry-portrange.html") {
                                        referencedObject = "AWS::EC2::NetworkAclEntry::Icmp"
                                        typeHref         = "aws-properties-ec2-networkaclentry-icmp.html"
                                    }

                                    deferredTasks.add(CrawlTask(referencedObject, typeHref!!))
                                }

                                propertyType = ((if (value.isEmpty()) value else value + " ") + referencedObject).trim()
                            } else if (em.text() == "Required") {
                                isRequired = "Yes" == value.trim()
                            }
                        }
                    }

                    // Bug
                    if (propertyName == "PolicyName" && uri == "aws-properties-ec2-elb-LBCookieStickinessPolicy.html") {
                        propertyType = "String"
                        isRequired   = true
                    }

                    if (propertyType == null) {
                        throw IllegalStateException("Can't parse property $propertyName type in $uri")
                    }
                    if (isRequired == null) {
                        throw IllegalStateException("Can't parse whether $propertyName is required in $uri")
                    }

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
            sourceURL    = BaseURL + uri,
            resourceType = resourceType,
            properties   = resourceProperties
        ).apply {
            crawledResources[resourceType]?.let { existingResource ->
                if (existingResource.sourceURL != this@apply.sourceURL) {
                    println(" *** Conflicting resource with key $resourceType:")
                    println(" *** ${existingResource.sourceURL}")
                    println(" *** ${this@apply.sourceURL}")
                }
            }
            crawledResources[resourceType] = this@apply

            deferredTasks.forEach {
                crawlResourceType(it.resourceType, it.uri)
            }
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