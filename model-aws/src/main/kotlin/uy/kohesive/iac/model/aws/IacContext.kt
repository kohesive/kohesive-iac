package uy.kohesive.iac.model.aws

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.CreatePolicyRequest
import com.amazonaws.services.identitymanagement.model.CreateRoleRequest
import uy.kohesive.iac.model.aws.DeferredAmazonEC2
import uy.kohesive.iac.model.aws.ParameterizedValue
import java.util.*

class IacContext(
    val environment: String,
    val planId: String,
    val namingStrategy: IacContextNamingStrategy = IacSimpleEnvPrefixNamingStrategy(),
    init: IacContext.()->Unit = {}
) {

    private val objectsToIds = IdentityHashMap<Any, String>()
    private val variables: MutableMap<String, ParameterizedValue> = hashMapOf()
    private val mappings: MutableMap<String, MappedValues> = hashMapOf()

    val ec2Client: AmazonEC2 by lazy { DeferredAmazonEC2(this) }
    val iamClient: AmazonIdentityManagement by lazy { DeferredAmazonIdentityManagement(this) }

    init {
        init()
    }

    fun getId(obj: Any): String?  = objectsToIds[obj]

    fun <T : AmazonWebServiceRequest> T.withKohesiveId(id: String): T = apply {
        objectsToIds[this@withKohesiveId] = id
    }

    // only we do random things like this, so don't pollute the user's AmazonWebServiceResult.* namespace
    internal fun <K: ResponseMetadata, T : AmazonWebServiceResult<out K>> T.withKohesiveId(id: String): T = apply {
        objectsToIds[this@withKohesiveId] = id
    }

    // only we do random things like this, so don't pollute the user's Any.* namespace
    internal fun <T : Any> T.withKohesiveId(id: String): T = apply {
        objectsToIds[this@withKohesiveId] = id
    }

    fun addVariables(vararg vari: ParameterizedValue) {
        variables.putAll(vari.map { it.name to it})
    }

    fun addMappings(vararg maps: MappedValues) {
        mappings.putAll(maps.map { it.name to it})
    }

    fun build(builder: IacContext.()->Unit) {
        this.builder()
    }


    // ===[ Stuff we would like to put somewhere else but cannot (i.e. has two recievers)]==============================

    // TODO: this needs to be somewhere else but has 2 receivers
    fun CreateRoleRequest.withKohesiveIdFromName(): CreateRoleRequest = apply {
        withKohesiveId(this.roleName)
    }

    // TODO: this needs to be somewhere else but has 2 receivers
    fun CreatePolicyRequest.withKohesiveIdFromName(): CreatePolicyRequest = apply {
        withKohesiveId(this.policyName)
    }
}