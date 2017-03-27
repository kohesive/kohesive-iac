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

    fun getParentClassName() = if (isRootClass()) {
        throw IllegalStateException()
    } else {
        name.split('.').dropLast(1).joinToString(".")
    }
}

data class AmazonCFClassProperty(
    val name: String,
    val type: String,
    val optional: Boolean
)

fun CloudFormationResource.toAmazonClass() = resourceType.split("::").drop(2).joinToString(".").let { className ->
    AmazonCFClass(
        simpleName = className.split('.').last(),
        name       = className,
        awsType    = resourceType,
        properties = properties.map { it.toAmazonClassProperty(className) }
    )
}

fun ResourceProperty.toAmazonClassProperty(containingClassName: String) = AmazonCFClassProperty(
    name     = propertyName,
    type     = propertyType.convertAwsTypeReference(containingClassName),
    optional = !required
)

fun String.convertAwsTypeReference(containingClassName: String): String {
    // TODO: implement
    return this
}

class ModelBuilder(val resources: List<CloudFormationResource>) {

    fun build(): List<AmazonCFClass> {
        val resourcesMap = mutableMapOf<String, AmazonCFClass>()

        resources.sortedBy { (_, resourceType) ->
            resourceType.split("::").size // root classes first
        }.map { it.toAmazonClass() }.forEach { amazonClass ->
            resourcesMap[amazonClass.name] = amazonClass

            // Add this class to its parent
            if (!amazonClass.isRootClass()) {
                resourcesMap.getOrElse(amazonClass.getParentClassName()) {
                    throw IllegalStateException("Missing ${amazonClass.getParentClassName()} class")
                }.innerClasses += amazonClass
            }
        }

        return resourcesMap.values.filter { it.isRootClass() }
    }

}