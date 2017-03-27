package uy.kohesive.iac.model.aws.cloudformation.codegen

import uy.kohesive.iac.model.aws.cloudformation.crawler.CloudFormationResource
import uy.kohesive.iac.model.aws.cloudformation.crawler.ResourceProperty

data class AmazonCFServiceModel(
    val packageName: String,
    val classes: List<AmazonCFClass>
)

data class AmazonCFClass(
    val name: String,
    val simpleName: String,
    val awsType: String,
    val properties: List<AmazonCFClassProperty>,
    val innerClasses: MutableList<AmazonCFClass> = mutableListOf()
) {
    fun isRootClass() = !name.contains('.')
}

data class AmazonCFClassProperty(
    val name: String,
    val type: String,
    val optional: Boolean
)

fun CloudFormationResource.toAmazonClass() = AmazonCFClass(
    simpleName = resourceType.split("::".drop(2)).last(),
    name       = resourceType.split("::".drop(2)).joinToString("."),
    awsType    = resourceType,
    properties = properties.map { it.toAmazonClassProperty() }
)

fun ResourceProperty.toAmazonClassProperty() = AmazonCFClassProperty(
    name     = propertyName,
    type     = propertyType.convertAwsTypeReference(),
    optional = !required
)

fun String.convertAwsTypeReference(): String {
    // TODO: implement
    return this
}

class ModelBuilder(val resources: List<CloudFormationResource>) {

    fun build(): List<AmazonCFClass> {
        val res = ArrayList<AmazonCFClass>()

        val resourcesMap = mutableMapOf<String, CloudFormationResource>()

        resources.sortedBy { (_, resourceType) ->
            resourceType.split("::").size // root classes first
        }.map { it.toAmazonClass() }.forEach { amazonClass ->
            resourcesMap[amazonClass.name]
        }

        // TODO: remove
        res.forEach { println() }
        println()
        return res
    }

}

