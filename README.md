# kohesive-iac
Infrastructure as code, Kotlin code.

THIS IS A WORK IN PROGRESS, NO RELEASES OR INFORMATION IS YET AVAILABLE

### Building CloudFormation templates with AWS API DSL

```kotlin
val hashKeyNameParam = ParameterizedValue.newString("HaskKeyElementName",
    description           = "HashType PrimaryKey Name",
    allowedPattern        = "[a-zA-Z0-9]*".toRegex(),
    allowedLength         = 1..2048,
    constraintDescription = "Must contain only alphanumberic characters"
)
...
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

TemplateBuilder(context, description = "This template demonstrates the creation of a DynamoDB table.").build()
```        
Results in the following CloudFormation template:
```json
{
  "AWSTemplateFormatVersion" : "2010-09-09",
  "Description" : "This template demonstrates the creation of a DynamoDB table.",
  "Parameters" : {
    "HaskKeyElementName" : {
      "Description" : "HashType PrimaryKey Name",
      "Type" : "String",
      "MinLength" : "1",
      "MaxLength" : "2048",
      "AllowedPattern" : "[a-zA-Z0-9]*",
      "ConstraintDescription" : "Must contain only alphanumberic characters"
    },
    ...
  },
  "Resources" : {
    "myDynamoDBTable" : {
      "Type" : "AWS::DynamoDB::Table",
      "Properties" : {
        "AttributeDefinitions" : [ {
          "AttributeName" : {
            "Ref" : "HaskKeyElementName"
          },
          "AttributeType" : {
            "Ref" : "HaskKeyElementType"
          }
        } ],
        "KeySchema" : [ {
          "AttributeName" : {
            "Ref" : "HaskKeyElementName"
          },
          "KeyType" : "HASH"
        } ],
        "ProvisionedThroughput" : {
          "ReadCapacityUnits" : {
            "Ref" : "ReadCapacityUnits"
          },
          "WriteCapacityUnits" : {
            "Ref" : "WriteCapacityUnits"
          }
        }
      }
    }
  },
  "Outputs" : {
    "TableName" : {
      "Description" : "Table name of the newly create DynamoDB table",
      "Value" : {
        "Ref" : "myDynamoDBTable"
      }
    }
  }
}
```   
        

