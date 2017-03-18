package uy.kohesive.iac.model.aws

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import junit.framework.TestCase
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationContext
import uy.kohesive.iac.model.aws.cloudformation.TemplateBuilder
import uy.kohesive.iac.model.aws.utils.CasePreservingJacksonNamingStrategy

class TestUseCase_DynamoDB_Table_1 : TestCase() {

    fun `test making a DynamoDB table using the SDK`() {
        // ===[ VARIABLES ]=============================================================================================

        val hashKeyNameParam = ParameterizedValue.newString("HaskKeyElementName",
            description           = "HashType PrimaryKey Name",
            allowedPattern        = "[a-zA-Z0-9]*".toRegex(),
            allowedLength         = 1..2048,
            constraintDescription = "Must contain only alphanumberic characters"
        )

        val hashKeyTypeParam = ParameterizedValue.newString("HaskKeyElementType",
            description           = "HashType PrimaryKey Type",
            defaultValue          = "S",
            allowedPattern        = "[S|N]".toRegex(),
            allowedLength         = 1..1,
            constraintDescription =  "Must be either S or N"
        )

        val readCapacityParam = ParameterizedValue.newLong("ReadCapacityUnits",
            description           = "Provisioned read throughput",
            defaultValue          = 5,
            allowedNumericValues  = 5L..10000L,
            constraintDescription = "Should be between 5 and 10000"
        )

        val writeCapacityParam = ParameterizedValue.newLong("WriteCapacityUnits",
            description           = "Provisioned read throughput",
            defaultValue          = 10,
            allowedNumericValues  = 5L..10000L,
            constraintDescription = "Should be between 5 and 10000"
        )

        // ===[ BUILDING ]==============================================================================================

        val context = CloudFormationContext("test", "dynamodb-table-myDynamoDBTable") {
            addVariables(hashKeyNameParam, hashKeyTypeParam, readCapacityParam, writeCapacityParam)

            withDynamoDBContext {
                val table = createTable("myDynamoDBTable") {
                    withKeySchema(KeySchemaElement(hashKeyNameParam.value, KeyType.HASH))
                    withAttributeDefinitions(AttributeDefinition(hashKeyNameParam.value, hashKeyTypeParam.value))
                    withProvisionedThroughput(ProvisionedThroughput()
                        .withReadCapacityUnits(readCapacityParam.value)
                        .withWriteCapacityUnits(writeCapacityParam.value)
                    )
                }

                addAsOutput("TableName", table, "Table name of the newly create DynamoDB table")
            }
        }

        val JsonWriter = jacksonObjectMapper()
            .setPropertyNamingStrategy(CasePreservingJacksonNamingStrategy())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .writerWithDefaultPrettyPrinter()

        val cfTemplate = TemplateBuilder(context, description = "AWS CloudFormation Sample Template: This template demonstrates the creation of a DynamoDB table.").build()

        println(JsonWriter.writeValueAsString(cfTemplate))
    }

}