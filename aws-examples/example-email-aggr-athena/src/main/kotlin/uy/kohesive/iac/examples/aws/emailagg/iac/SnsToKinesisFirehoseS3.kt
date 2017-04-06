package uy.kohesive.iac.examples.aws.emailagg.iac

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.Policy
import com.amazonaws.services.identitymanagement.model.Role
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.contexts.IdentityManagementContext
import uy.kohesive.iac.model.aws.helpers.*

class SnsToKinesisFirehoseS3(val context: IacContext) {
    private fun <T: Any> iam(init: IdentityManagementContext.(AmazonIdentityManagement) -> T): T = context.withIdentityManagementContext(init)

    fun createSnsToS3ViaFirehoseAdapter(baseName: String) {
        iamRoleForSnsToKinesisFirehoseLambda("$baseName-role", "$baseName-logging-policy", "$baseName-kinesis-put-policy")
    }

    fun allowLoggingPolicyDocument(): PolicyDocument<CustomPolicyStatement> {
                // TODO: a bit ugly, we should DSL a policy doc
                // TODO: we should group and have enums or a ton of prebuilt policy statements to re-use as ENUM or something
           return PolicyDocument(listOf(CustomPolicyStatement(PolicyEffect.Allow,
                        listOf("logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents"),
                        "arn:aws:logs:*:*:*")))
    }

    fun allowPutToFirehosePolicyDocument(kinesisFirehoseArn: String): PolicyDocument<CustomPolicyStatement> {
        return PolicyDocument(listOf(CustomPolicyStatement(PolicyEffect.Allow,
                listOf("firehose:PutRecord", "firehose:PutRecordBatch"),
                kinesisFirehoseArn)))
    }
/*
    fun allowFirehoseS3Access(bucketName: String, keyPrefix: String): PolicyDocument<CustomPolicyStatement> {

    }
*/
    fun allowLoggingPolicy(policyName: String): Policy {
       return iam {
           // TODO: sometimes you want to create something that only has 1 instance in AWS across stacks, should we allow that, or is every stack
           //       only self contained?
           createPolicy(policyName) {
              policyFromDocument = allowLoggingPolicyDocument()
           }
       }
    }

    fun allowPutToFirehosePolicy(kinesisFirehoseName: String,
                                 kinesisFirehoseArn: String,
                                 policyName: String = "policy-allow-put-to-$kinesisFirehoseName"): Policy {
        // TODO:  again, if we have this policy already do we really want another copy?
        return iam {
            createPolicy(policyName) {
                policyFromDocument = allowPutToFirehosePolicyDocument(kinesisFirehoseArn)
            }
        }
    }

    fun iamRoleForSnsToKinesisFirehoseLambda(roleName: String,
                                             firehoseName: String,
                                             firehoseArn: String,
                                             loggingPolicyName: String = "$roleName-logging-policy",
                                             kinesisPutPolicyName: String = "$roleName-kinesis-put-policy"): Role {
        return iam {
            createRole(roleName) {
                assumeRoleFromPolicyDocument = PolicyDocument(listOf(AssumeRolePolicyStatement("lambda.amazonaws.com")))
            }.also { role ->
                attachIamRolePolicy(role, allowLoggingPolicy(loggingPolicyName))
                attachIamRolePolicy(role, allowPutToFirehosePolicy(firehoseName, firehoseArn, kinesisPutPolicyName))
            }
        }
    }


}