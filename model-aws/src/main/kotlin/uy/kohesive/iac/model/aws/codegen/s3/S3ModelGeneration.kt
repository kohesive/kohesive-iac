package uy.kohesive.iac.model.aws.codegen.s3

import com.amazonaws.codegen.C2jModels
import com.amazonaws.codegen.CodeGenerator
import com.amazonaws.codegen.model.config.BasicCodeGenConfig
import com.amazonaws.codegen.model.config.customization.CustomizationConfig
import com.amazonaws.codegen.model.service.*
import com.amazonaws.http.HttpMethodName
import com.amazonaws.services.s3.AmazonS3
import org.reflections.Reflections
import org.reflections.scanners.TypeElementsScanner
import uy.klutter.core.common.mustNotEndWith
import uy.klutter.core.common.mustNotStartWith
import uy.kohesive.iac.model.aws.utils.namespace
import uy.kohesive.iac.model.aws.utils.simpleName

fun main(args: Array<String>) {
    val outputDir = "/Users/eliseyev/TMP/codegen/s3/"

    val (interfaceSimpleName, packageName) = AmazonS3::class.qualifiedName!!.let {
        it.simpleName() to it.namespace()
    }

    val modelPackage = packageName + ".model"

    val typeElementsScanner = TypeElementsScanner()
    Reflections(modelPackage, typeElementsScanner)

    val codeGenConfig = BasicCodeGenConfig(
        interfaceSimpleName,
        packageName,
        null,
        null,
        null
    )

    val serviceMetadata = ServiceMetadata().apply {
        protocol            = "json"
        apiVersion          = "2006-03-01"
        endpointPrefix      = "s3"
        jsonVersion         = "1.0"
        protocol            = "json"
        isResultWrapped     = false
        serviceAbbreviation = "S3"
        serviceFullName     = "Amazon S3"
        signatureVersion    = "v4"
        signingName         = "s3"
        targetPrefix        = "S3_20060301"
        uid                 = "s3-2006-03-01"
    }

    val OperationPrefixToHttpMethod = listOf(
        "Restore"   to HttpMethodName.POST,
        "Set"       to HttpMethodName.PUT,
        "Generate"  to HttpMethodName.GET,
        "List"      to HttpMethodName.GET,
        "Get"       to HttpMethodName.GET,
        "Delete"    to HttpMethodName.DELETE,
        "Copy"      to HttpMethodName.PUT,
        "Head"      to HttpMethodName.HEAD,
        "Abort"     to HttpMethodName.DELETE,
        "Encrypted" to HttpMethodName.POST,
        "Create"    to HttpMethodName.PUT,
        "Initiate"  to HttpMethodName.POST,
        "Upload"    to HttpMethodName.POST,
        "Complete"  to HttpMethodName.POST,
        "Put"       to HttpMethodName.PUT
    )

    // Init the shapes map with primitives
    val shapes = HashMap<String, Shape>().apply {
        listOf(
            java.lang.String::class.java,
            java.lang.Integer::class.java,
            java.lang.Long::class.java,
            java.lang.Boolean::class.java
        ).forEach { autoboxType ->
            put(autoboxType.simpleName, Shape().apply {
                type = autoboxType.simpleName.toLowerCase()
            })
        }
    }

    fun createShape(shapeName: String, classFqName: String) = Shape().apply {
        val clazz = try { Class.forName(classFqName) } catch (cnf: ClassNotFoundException) { Void::class.java }

        val membersByGetters = clazz.declaredMethods.filter { method ->
            method.name.startsWith("get") || method.name.startsWith("is")
        }.map { getter ->
            getter.name.mustNotStartWith("get").mustNotStartWith("is") to getter.returnType
        }.toMap()

        members = membersByGetters.mapValues {
            val memberShape = if (it.value.isPrimitive) {
                it.value.name
            } else if (it.value.name.startsWith("java.lang.")) {
                it.value.simpleName
            } else if (List::class.java.isAssignableFrom(it.value)) {
                it.key + "List" // TODO: save the shape
            } else if (Set::class.java.isAssignableFrom(it.value)) {
                it.key + "Set" // TODO: save the shape
            } else if (Map::class.java.isAssignableFrom(it.value)) {
                it.key + "Map" // TODO: save the shape
            } else {
                // TODO: save the shape
                it.value.simpleName
            }

            Member().apply {
                shape = memberShape
            }
        }

        type = "structure"

        shapes[shapeName] = this@apply
    }

    val requestClassNames = typeElementsScanner.store.keySet().filter {
        it.endsWith("Request")
    }
    val operations = requestClassNames.filterNot { requestFqName ->
        requestFqName.simpleName().let { simpleName ->
            simpleName.startsWith("Abstract") || simpleName.startsWith("Generic")
        }
    }.map { requestFqName ->
        requestFqName.simpleName().mustNotEndWith("Request").let { operationName ->
            val httpMethod = OperationPrefixToHttpMethod.firstOrNull { operationName.startsWith(it.first) }?.second?.name
                ?: throw IllegalStateException("Can't figure out HTTP method for $operationName")

            val http = Http()
                .withMethod(httpMethod)
                .withRequestUri("/")

            val input = Input().apply {
                shape = operationName + "Input"
            }
            val output = Output().apply {
                shape = operationName + "Output"
            }

            createShape(input.shape, requestFqName)
            createShape(output.shape, requestFqName.mustNotEndWith("Request") + "Result")

            val operation = Operation()
                .withName(operationName)
                .withInput(input)
                .withHttp(http).apply { setOutput(output) }

            operationName to operation
        }
    }.toMap()

    val c2jModels = C2jModels.builder()
        .codeGenConfig(codeGenConfig)
        .customizationConfig(CustomizationConfig.DEFAULT)
        .serviceModel(ServiceModel(
            serviceMetadata,
            operations,
            shapes,
            HashMap<String, Authorizer>() // TODO: add authorizers
        )
    ).build()

    CodeGenerator(c2jModels, outputDir, outputDir, "s3").execute()
}