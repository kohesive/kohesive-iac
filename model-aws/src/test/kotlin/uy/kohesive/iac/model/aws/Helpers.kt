package uy.kohesive.iac.model.aws

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*


// ===[HELPERS, things to turn into extensinos]=====================================================================

fun AmazonIdentityManagement.createRole(init: CreateRoleRequest.()->Unit): CreateRoleResult {
    return createRole(CreateRoleRequest().apply { this.init() })
}

fun AmazonIdentityManagement.createPolicy(init: CreatePolicyRequest.()->Unit): CreatePolicyResult {
    return createPolicy(CreatePolicyRequest().apply { this.init() })
}

fun AmazonIdentityManagement.attachRolePolicy(init: AttachRolePolicyRequest.()->Unit): AttachRolePolicyResult {
    return attachRolePolicy(AttachRolePolicyRequest().apply { this.init() })
}

enum class AssumeRolePolicies(val policyDoc: AssumeRolePolicyStatement) {
    EC2(AssumeRolePolicyStatement("ec2.amazonaws.com")),
    Lambda(AssumeRolePolicyStatement("lambda.amazonaws.com")),
    SQS(AssumeRolePolicyStatement("sqs.amazonaws.com")),
    KinesisFirehose(AssumeRolePolicyStatement("firehose.amazonaws.com"));

    fun asPolicyDoc(): PolicyDocument<AssumeRolePolicyStatement> = PolicyDocument(listOf(policyDoc))
    override fun toString(): String = asPolicyDoc().toString()
}

fun CreateRoleRequest.withAssumeRolePolicyDocument(document: PolicyDocument<AssumeRolePolicyStatement>): CreateRoleRequest = apply {
    withAssumeRolePolicyDocument(document.toString())
}

fun CreateRoleRequest.withAssumeRolePolicyDocument(statement: AssumeRolePolicyStatement): CreateRoleRequest = apply {
    withAssumeRolePolicyDocument(PolicyDocument(listOf(statement)).toString())
}

fun CreateRoleRequest.withAssumeRolePolicyDocument(statements: List<AssumeRolePolicyStatement>): CreateRoleRequest = apply {
    withAssumeRolePolicyDocument(PolicyDocument(statements).toString())
}

fun CreatePolicyRequest.withPolicyDocument(document: PolicyDocument<CustomPolicyStatement>): CreatePolicyRequest = apply {
    withPolicyDocument(document.toString())
}

fun CreatePolicyRequest.withPolicyDocument(statement: CustomPolicyStatement): CreatePolicyRequest = apply {
    withPolicyDocument(PolicyDocument(listOf(statement)).toString())
}


fun CreatePolicyRequest.withPolicyDocument(statements: List<CustomPolicyStatement>): CreatePolicyRequest = apply {
    withPolicyDocument(PolicyDocument(statements).toString())
}


enum class PolicyEffect { Allow, Deny }
abstract class PolicyStatement(val effect: PolicyEffect) {
    abstract val body: String

    override fun toString(): String {
        return body
    }
}

fun List<String>.mapQuoted(): List<String> = this.map { "\"" + it + "\"" }
fun List<String>.toQuotedCommaDelimitedString(): String = this.mapQuoted().joinToString(", ")

class CustomPolicyStatement(val actions: List<String>, effect: PolicyEffect, val resources: List<String>): PolicyStatement(effect) {
    constructor (action: String, effect: PolicyEffect, resource: String) : this(listOf(action), effect, listOf(resource))
    constructor (action: String, effect: PolicyEffect, resources: List<String>) : this(listOf(action), effect, resources)
    constructor (actions: List<String>, effect: PolicyEffect, resource: String): this(actions, effect, listOf(resource))

    override val body: String get() = """
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

class AssumeRolePolicyStatement(val principal: String): PolicyStatement(PolicyEffect.Allow) {
    override val body: String get() = """
              {
                "Effect" : "${effect}",
                "Principal" : {
                  "Service" : [ "${principal}" ]
                },
                "Action" : [ "sts:AssumeRole" ]
              }
        """
}

class PolicyDocument<STATEMENT_TYPE: PolicyStatement>(val statements: List<STATEMENT_TYPE>, val version: String = "2012-10-17") {
    override fun toString(): String {
        return """
              {
                  "Version": "${version},
                  "Statement": [
                     ${statements.map { it.toString() }.joinToString(",\n")}
                  ]
              }
            """
    }
}