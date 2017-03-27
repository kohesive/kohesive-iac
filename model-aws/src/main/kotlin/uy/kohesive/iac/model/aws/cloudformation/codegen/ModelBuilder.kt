package uy.kohesive.iac.model.aws.cloudformation.codegen

import uy.kohesive.iac.model.aws.cloudformation.crawler.CloudFormationResource
import uy.kohesive.iac.model.aws.cloudformation.crawler.ResourceProperty

data class AmazonCFServiceModel(
    val packageName: String,
    val serviceName: String,
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

fun CloudFormationResource.toAmazonClass() = resourceType.cfAWSTypeToClassName().let { className ->
    AmazonCFClass(
        simpleName = className.split('.').last(),
        name       = className,
        awsType    = resourceType,
        properties = properties.map { it.toAmazonClassProperty(resourceType) }
    )
}

fun ResourceProperty.toAmazonClassProperty(containingAwsType: String) = AmazonCFClassProperty(
    name     = propertyName,
    type     = propertyType.convertTypeReference(containingAwsType),
    optional = !required
)

fun String.convertTypeReference(containingAwsType: String): String {
    if (!contains("AWS::")) {
        return this.replace("Number", "String").replace("JsonObject", "String")
    }
    val sourceNamespace = containingAwsType.split("::").take(2).joinToString("::")
    if (startsWith("AWS::")) {
        return cfAWSTypeToClassName(dropNamespace = startsWith(sourceNamespace))
    }
    if (startsWith("List<AWS::")) {
        val listTypeParameter = drop("List<".length).dropLast(1)
        return "List<${ listTypeParameter.cfAWSTypeToClassName(dropNamespace = startsWith(sourceNamespace)) }>"
    }
    throw IllegalArgumentException("Illegal type: $this")
}

fun String.cfAWSTypeToClassName(dropNamespace: Boolean = true)
    = split("::").drop(if (dropNamespace) 2 else 1).mapIndexed { index, classPart ->
        if (index > if (dropNamespace) 0 else 1) {
            classPart + "Property"
        } else {
            classPart
        }
    }.joinToString(".")

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