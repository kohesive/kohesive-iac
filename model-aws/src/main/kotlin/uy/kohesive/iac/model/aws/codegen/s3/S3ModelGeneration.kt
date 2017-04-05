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
import uy.klutter.core.jdk.mustNotEndWith
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

    val requestClassNames = typeElementsScanner.store.keySet().filter {
        it.endsWith("Request")
    }
    val operations = requestClassNames.filterNot {
        it.simpleName().let { simpleName ->
            simpleName.startsWith("Abstract") || simpleName.startsWith("Generic")
        }
    }.map {
        it.simpleName().mustNotEndWith("Request").let { operationName ->
            val httpMethod = OperationPrefixToHttpMethod.firstOrNull { operationName.startsWith(it.first) }?.second?.name

            val http = Http()
                .withMethod(httpMethod)
                .withRequestUri("/")

            if (http.method == null) {
                throw IllegalStateException("Can't figure out HTTP method for $operationName")
            }

            val operation = Operation()
                .withName(operationName) // TODO: fill the object
                .withHttp(http)

            operationName to operation
        }
    }.toMap()

    val c2jModels = C2jModels.builder()
        .codeGenConfig(codeGenConfig)
        .customizationConfig(CustomizationConfig.DEFAULT)
        .serviceModel(ServiceModel(
            serviceMetadata,
            operations,
            HashMap<String, Shape>(),     // TODO: add shapes
            HashMap<String, Authorizer>() // TODO: add authorizers
        )
    ).build()

    CodeGenerator(c2jModels, outputDir, outputDir, "s3").execute()
}