package uy.kohesive.iac.model.aws.helpers

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.proxy.KohesiveReference
import uy.kohesive.iac.model.aws.proxy.isKohesiveRef
import uy.kohesive.iac.model.aws.utils.writeOnlyVirtualProperty

fun AmazonIdentityManagement.createRole(init: CreateRoleRequest.() -> Unit): CreateRoleResult {
    return createRole(CreateRoleRequest().apply { this.init() })
}

fun AmazonIdentityManagement.createPolicy(init: CreatePolicyRequest.() -> Unit): CreatePolicyResult {
    return createPolicy(CreatePolicyRequest().apply { this.init() })
}

fun AmazonIdentityManagement.attachRolePolicy(init: AttachRolePolicyRequest.() -> Unit): AttachRolePolicyResult {
    return attachRolePolicy(AttachRolePolicyRequest().apply { this.init() })
}

fun AmazonIdentityManagement.attachRolePolicy(roleResult: CreateRoleResult, policyResult: CreatePolicyResult): AttachRolePolicyResult {
    return attachRolePolicy {
        roleName = roleResult.role.roleName
        policyArn = policyResult.policy.arn
    }
}

fun AmazonIdentityManagement.attachRolePolicy(role: Role, policy: Policy): AttachRolePolicyResult {
    return attachRolePolicy {
        roleName = role.roleName
        policyArn = policy.arn
    }
}

fun AmazonIdentityManagement.createInstanceProfile(init: CreateInstanceProfileRequest.()->Unit): CreateInstanceProfileResult {
    return createInstanceProfile(CreateInstanceProfileRequest().apply { this.init() })
}


enum class AssumeRolePrincipals(val statement: AssumeRolePolicyStatement) {
    EC2(AssumeRolePolicyStatement("ec2.amazonaws.com")),
    Lambda(AssumeRolePolicyStatement("lambda.amazonaws.com")),
    SQS(AssumeRolePolicyStatement("sqs.amazonaws.com")),
    KinesisFirehose(AssumeRolePolicyStatement("firehose.amazonaws.com"));

    fun asPolicyDoc(): PolicyDocument<AssumeRolePolicyStatement> = statement.asPolicyDoc()
    override fun toString(): String = statement.asPolicyDoc().toJson()
}

fun CreateRoleRequest.withAssumeRolePolicyDocument(document: PolicyDocument<AssumeRolePolicyStatement>): CreateRoleRequest = apply {
    withAssumeRolePolicyDocument(document.toJson())
}

fun CreateRoleRequest.withAssumeRolePolicyDocument(principal: AssumeRolePrincipals): CreateRoleRequest = apply {
    withAssumeRolePolicyDocument(principal.asPolicyDoc())
}

fun CreateRoleRequest.withAssumeRolePolicyDocument(statement: AssumeRolePolicyStatement): CreateRoleRequest = apply {
    withAssumeRolePolicyDocument(statement.asPolicyDoc())
}

fun CreateRoleRequest.withAssumeRolePolicyDocument(statements: List<AssumeRolePolicyStatement>): CreateRoleRequest = apply {
    withAssumeRolePolicyDocument(statements.asPolicyDoc())
}

// TODO: is it better to create a builder class?

var CreateRoleRequest.assumeRoleFromPolicyDocument: PolicyDocument<AssumeRolePolicyStatement>
        by writeOnlyVirtualProperty(CreateRoleRequest::setAssumeRolePolicyDocument) { it.toJson() }

var CreateRoleRequest.assumeRoleFromPrincipal: AssumeRolePrincipals
        by writeOnlyVirtualProperty(CreateRoleRequest::setAssumeRolePolicyDocument) { it.asPolicyDoc().toJson() }

var CreateRoleRequest.assumeRoleFromPolicyStatement: AssumeRolePolicyStatement
        by writeOnlyVirtualProperty(CreateRoleRequest::setAssumeRolePolicyDocument) { it.asPolicyDoc().toJson() }

var CreateRoleRequest.assumeRoleFromPolicyStatements: List<AssumeRolePolicyStatement>
        by writeOnlyVirtualProperty(CreateRoleRequest::setAssumeRolePolicyDocument) { it.asPolicyDoc().toJson() }

fun CreatePolicyRequest.withPolicyDocument(document: PolicyDocument<CustomPolicyStatement>): CreatePolicyRequest = apply {
    withPolicyDocument(document.toJson())
}

fun CreatePolicyRequest.withPolicyDocument(statement: CustomPolicyStatement): CreatePolicyRequest = apply {
    withPolicyDocument(statement.asPolicyDoc())
}

fun CreatePolicyRequest.withPolicyDocument(statements: List<CustomPolicyStatement>): CreatePolicyRequest = apply {
    withPolicyDocument(statements.asPolicyDoc())
}

var CreatePolicyRequest.policyFromDocument: PolicyDocument<CustomPolicyStatement>
        by writeOnlyVirtualProperty(CreatePolicyRequest::setPolicyDocument) { it.toJson() }

var CreatePolicyRequest.policyFromStatement: CustomPolicyStatement
        by writeOnlyVirtualProperty(CreatePolicyRequest::setPolicyDocument) { it.asPolicyDoc().toJson() }

var CreatePolicyRequest.policyFromStatements: List<CustomPolicyStatement>
        by writeOnlyVirtualProperty(CreatePolicyRequest::setPolicyDocument) { it.asPolicyDoc().toJson() }

enum class PolicyEffect { Allow, Deny }

abstract class IsReallyJson {
    abstract val json: String

    override fun toString(): String = toJson()

    fun toJson(): String {
        return json
    }
}

abstract class PolicyStatement(val effect: PolicyEffect) : IsReallyJson() {

}

fun <T : AssumeRolePolicyStatement> T.asPolicyDoc(): PolicyDocument<T> = PolicyDocument(listOf(this))
@JvmName("asAssumePolicyDoc")
fun <T : AssumeRolePolicyStatement> List<T>.asPolicyDoc(): PolicyDocument<T> = PolicyDocument(this)

fun <T : CustomPolicyStatement> T.asPolicyDoc(): PolicyDocument<T> = PolicyDocument(listOf(this))
@JvmName("asCustomPolicyDoc")
fun <T : CustomPolicyStatement> List<T>.asPolicyDoc(): PolicyDocument<T> = PolicyDocument(this)

private fun List<String>.mapQuoted(): List<String> = this.map { "\"" + it + "\"" }
private fun List<String>.toQuotedCommaDelimitedString(): String = this.mapQuoted().joinToString(", ")

open class CustomPolicyStatement(effect: PolicyEffect, val actions: List<String>, val resources: List<String>) : PolicyStatement(effect) {
    constructor (effect: PolicyEffect, action: String, resource: String) : this(effect, listOf(action), listOf(resource))
    constructor (effect: PolicyEffect, action: String, resources: List<String>) : this(effect, listOf(action), resources)
    constructor (effect: PolicyEffect, actions: List<String>, resource: String) : this(effect, actions, listOf(resource))

    override val json: String get() = """
              {
                "Effect" : "${effect}",
                "Action" : [
                     ${actions.toQuotedCommaDelimitedString()}
                ],
                "Resource" : [
                     ${resources.toQuotedCommaDelimitedString()}
                ]
              }
        """
}

open class AssumeRolePolicyStatement(val principal: String) : PolicyStatement(PolicyEffect.Allow) {
    override val json: String get() = """
              {
                "Effect" : "${effect}",
                "Principal" : {
                  "Service" : [ "${principal}" ]
                },
                "Action" : [ "sts:AssumeRole" ]
              }
        """
}

class PolicyDocument<STATEMENT_TYPE : PolicyStatement>(val statements: List<STATEMENT_TYPE>, val version: String = "2012-10-17") : IsReallyJson() {
    override val json: String get() = """
              {
                  "Version": "${version}",
                  "Statement": [
                     ${statements.map { it.toJson() }.joinToString(",\n")}
                  ]
              }
            """
}

fun AttachRolePolicyRequest.getPolicyNameFromArn() = if (policyArn.isKohesiveRef()) {
    KohesiveReference.fromString(policyArn).targetId
} else {
    // TODO: implement
    throw RuntimeException("Can't figure out policy name from ARN")
}
