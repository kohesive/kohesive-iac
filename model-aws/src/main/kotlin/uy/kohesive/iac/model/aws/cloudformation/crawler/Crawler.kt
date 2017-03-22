package uy.kohesive.iac.model.aws.cloudformation.crawler

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.TextNode
import uy.klutter.core.jdk.mustNotStartWith
import java.io.File
import java.net.SocketTimeoutException

fun main(args: Array<String>) {
    Crawler(
        baseUri   = "/Users/eliseyev/Desktop/",
        localMode = true
    ).crawl()
}

data class ResourceUri(
    val resourceType: String,
    val uri: String
)

data class CloudFormationResource(
    val resourceType: String,
    val properties: List<ResourceProperty>
)

data class ResourceProperty(
    val propertyName: String,
    val propertyType: String,
    val isRequired: Boolean,
    val propertyHref: String? = null
)

class Crawler(val baseUri: String = Crawler.BaseURI, val localMode: Boolean = false) {

    companion object {
        val BaseURI = "http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/"
    }

    fun getJsoupDocument(uri: String) = if (localMode) {
        getJsoupDocumentFromFile(baseUri + uri)
    } else {
        getJsoupDocumentForUrl(baseUri + uri)
    }

    fun crawl() {
        val resourcesListDoc = getJsoupDocument("aws-template-resource-type-ref.html")

        val resourceUris = resourcesListDoc.select(".highlights li a").map {
            ResourceUri(
                resourceType = it.text(),
                uri          = it.attr("href")
            )
        }

        // TODO: delete take(1)
        resourceUris.take(1).forEach { (resourceType, uri) ->
            val resourceDoc = getJsoupDocument(uri)

            val resourceProperties = resourceDoc.select(".variablelist").flatMap { varListDiv ->
                val variablesType = varListDiv.previousElementSibling().text()
                if (setOf("Properties", "Parameters", "Members").contains(variablesType)) {
                    varListDiv.select("dl dt").map { propertyDt ->
                        val propertyName = propertyDt.text().trim()

                        var propertyType: String? = null
                        var isRequired: Boolean? = null

                        propertyDt.nextElementSibling().select("em").forEach { em ->
                            em.text().let {
                                val value = (em.nextSibling() as TextNode).text().trim().mustNotStartWith("::").mustNotStartWith(":").trim()

                                if (em.text() == "Type") {
                                    // TODO: beware of href here
                                    propertyType = value
                                } else if (em.text() == "Required") {
                                    isRequired = "Yes" == value
                                }
                            }
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
                            isRequired   = isRequired!!
                            // TODO: complex type href
                        )
                    }
                } else {
                    emptyList()
                }
            }

            val resource = CloudFormationResource(
                resourceType = resourceType,
                properties   = resourceProperties
            )

            // TODO: store?

            println(resource)
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