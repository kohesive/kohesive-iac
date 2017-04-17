package uy.kohesive.iac.model.aws.codegen.s3

import com.amazonaws.HttpMethod
import com.amazonaws.codegen.model.service.ServiceMetadata
import com.amazonaws.http.HttpMethodName
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CORSRule
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.Permission
import com.amazonaws.services.s3.model.StorageClass
import uy.kohesive.iac.model.aws.codegen.model.EnumHandler
import uy.kohesive.iac.model.aws.codegen.model.ModelFromAPIGenerator

fun main(args: Array<String>) {
    S3ModelGenerator(
        outputDir         = "/Users/eliseyev/TMP/codegen/s3/",
        serviceSourcesDir = "/Users/eliseyev/git/aws-sdk-java/aws-java-sdk-s3/src/main/java"
    ).generate()
}

class S3ModelGenerator(
    val outputDir: String,
    val serviceSourcesDir: String
) {

    companion object {
        private val S3ServiceMetadata = ServiceMetadata().apply {
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

        private val S3OperationPrefixToHttpMethod: List<Pair<String, HttpMethodName>> = listOf(
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
    }

    fun generate() {
        ModelFromAPIGenerator(
            serviceInterface  = AmazonS3::class.java,
            outputDir         = outputDir,
            serviceMetadata   = S3ServiceMetadata,
            verbToHttpMethod  = S3OperationPrefixToHttpMethod.toMap(),
            fileNamePrefix    = S3ServiceMetadata.uid,
            serviceSourcesDir = serviceSourcesDir,
            enumHandlers      = listOf(
                EnumHandler(Permission::class.java, Permission.values().map { it.toString() }),
                EnumHandler(CannedAccessControlList::class.java, CannedAccessControlList.values().map { it.toString() }),
                EnumHandler(StorageClass::class.java, StorageClass.values().map { it.toString() }),
                EnumHandler(HttpMethod::class.java, HttpMethod.values().map { it.name }),
                EnumHandler(CORSRule.AllowedMethods::class.java, CORSRule.AllowedMethods.values().map { it.toString() })
            )
        ).generate()
    }

}