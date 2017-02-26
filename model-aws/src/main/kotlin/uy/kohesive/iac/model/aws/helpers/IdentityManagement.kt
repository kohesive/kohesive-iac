package uy.kohesive.iac.model.aws.helpers

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.*
import uy.kohesive.iac.model.aws.DeferredAmazonIdentityManagement
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable

interface IamRoleIdentifiable: KohesiveIdentifiable {
    fun CreateRoleRequest.withKohesiveIdFromName(): CreateRoleRequest = apply {
        withKohesiveId(this.roleName)
    }

    fun CreatePolicyRequest.withKohesiveIdFromName(): CreatePolicyRequest = apply {
        withKohesiveId(this.policyName)
    }
}

interface IamRoleEnabled: IamRoleIdentifiable {
    val iamClient: AmazonIdentityManagement
    val iamContext: IamContext
    fun withIamContext(init: IamContext.(AmazonIdentityManagement)->Unit) = iamContext.init(iamClient)
}

class IamContext(private val context: IacContext): IamRoleEnabled by context {
    fun IamContext.createRole(init: CreateRoleRequest.() -> Unit): CreateRoleResult {
        return iamClient.createRole(CreateRoleRequest().apply { init(); withKohesiveIdFromName() })
    }

    fun IamContext.createPolicy(init: CreatePolicyRequest.() -> Unit): CreatePolicyResult {
        return iamClient.createPolicy(CreatePolicyRequest().apply { this.init(); withKohesiveIdFromName() })
    }

    fun IamContext.attachRolePolicy(init: AttachRolePolicyRequest.() -> Unit): AttachRolePolicyResult {
        return iamClient.attachRolePolicy(AttachRolePolicyRequest().apply { this.init(); withKohesiveId(this.roleName + " => " + this.policyArn) })
    }

    fun IamContext.attachIamRolePolicy(roleResult: CreateRoleResult, policyResult: CreatePolicyResult): AttachRolePolicyResult {
        return attachRolePolicy {
            roleName = roleResult.role.roleName
            policyArn = policyResult.policy.arn
        }
    }

    fun IamContext.attachIamRolePolicy(role: Role, policy: Policy): AttachRolePolicyResult {
        return attachRolePolicy {
            roleName = role.roleName
            policyArn = policy.arn
        }
    }
}

// ===[ General Helpers ]===============================================================================================

fun AmazonIdentityManagement.createRole(init: CreateRoleRequest.()->Unit): CreateRoleResult {
    return createRole(CreateRoleRequest().apply { this.init() })
}

fun AmazonIdentityManagement.createPolicy(init: CreatePolicyRequest.()->Unit): CreatePolicyResult {
    return createPolicy(CreatePolicyRequest().apply { this.init() })
}

fun AmazonIdentityManagement.attachRolePolicy(init: AttachRolePolicyRequest.()->Unit): AttachRolePolicyResult {
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

enum class AssumeRolePrincipals(val statement: AssumeRolePolicyStatement) {
    EC2(AssumeRolePolicyStatement("ec2.amazonaws.com")),
    Lambda(AssumeRolePolicyStatement("lambda.amazonaws.com")),
    SQS(AssumeRolePolicyStatement("sqs.amazonaws.com")),
    KinesisFirehose(AssumeRolePolicyStatement("firehose.amazonaws.com"));

    fun asPolicyDoc(): PolicyDocument<AssumeRolePolicyStatement> = statement.asPolicyDoc()
    override fun toString(): String = statement.asPolicyDoc().toString()
}

fun CreateRoleRequest.withAssumeRolePolicyDocument(document: PolicyDocument<AssumeRolePolicyStatement>): CreateRoleRequest = apply {
    withAssumeRolePolicyDocument(document.toString())
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
    get() = throw IllegalStateException("This is a write only property")
    set(value) { withAssumeRolePolicyDocument(value) }

var CreateRoleRequest.assumeRoleFromPrincipal: AssumeRolePrincipals
    get() = throw IllegalStateException("This is a write only property")
    set(value) { withAssumeRolePolicyDocument(value) }

var CreateRoleRequest.assumeRoleFromPolicyStatement: AssumeRolePolicyStatement
    get() = throw IllegalStateException("This is a write only property")
    set(value) { withAssumeRolePolicyDocument(value) }

var CreateRoleRequest.assumeRoleFromPolicyStatements: List<AssumeRolePolicyStatement>
    get() = throw IllegalStateException("This is a write only property")
    set(value) { withAssumeRolePolicyDocument(value) }

fun CreatePolicyRequest.withPolicyDocument(document: PolicyDocument<CustomPolicyStatement>): CreatePolicyRequest = apply {
    withPolicyDocument(document.toString())
}

fun CreatePolicyRequest.withPolicyDocument(statement: CustomPolicyStatement): CreatePolicyRequest = apply {
    withPolicyDocument(statement.asPolicyDoc())
}

fun CreatePolicyRequest.withPolicyDocument(statements: List<CustomPolicyStatement>): CreatePolicyRequest = apply {
    withPolicyDocument(statements.asPolicyDoc())
}

var CreatePolicyRequest.policyFromDocument: PolicyDocument<CustomPolicyStatement>
    get() = throw IllegalStateException("This is a write only property")
    set(value) { withPolicyDocument(value) }

var CreatePolicyRequest.policyFromStatement: CustomPolicyStatement
    get() = throw IllegalStateException("This is a write only property")
    set(value) { withPolicyDocument(value) }

var CreatePolicyRequest.policyFromStatements: List<CustomPolicyStatement>
    get() = throw IllegalStateException("This is a write only property")
    set(value) { withPolicyDocument(value) }

enum class PolicyEffect { Allow, Deny }
abstract class PolicyStatement(val effect: PolicyEffect) {
    abstract val body: String

    override fun toString(): String {
        return body
    }
}

fun <T: AssumeRolePolicyStatement> T.asPolicyDoc(): PolicyDocument<T> = PolicyDocument(listOf(this))
@JvmName("asAssumePolicyDoc")
fun <T: AssumeRolePolicyStatement> List<T>.asPolicyDoc(): PolicyDocument<T> = PolicyDocument(this)
fun <T: CustomPolicyStatement> T.asPolicyDoc(): PolicyDocument<T> = PolicyDocument(listOf(this))
@JvmName("asCustomPolicyDoc")
fun <T: CustomPolicyStatement> List<T>.asPolicyDoc(): PolicyDocument<T> = PolicyDocument(this)

private fun List<String>.mapQuoted(): List<String> = this.map { "\"" + it + "\"" }
private fun List<String>.toQuotedCommaDelimitedString(): String = this.mapQuoted().joinToString(", ")

open class CustomPolicyStatement(val actions: List<String>, effect: PolicyEffect, val resources: List<String>): PolicyStatement(effect) {
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

open class AssumeRolePolicyStatement(val principal: String): PolicyStatement(PolicyEffect.Allow) {
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