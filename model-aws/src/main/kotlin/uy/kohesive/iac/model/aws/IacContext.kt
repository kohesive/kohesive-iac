package uy.kohesive.iac.model.aws

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
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

    fun <T : Any> T.withId(id: String): T = apply {
        objectsToIds[this@withId] = id
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
}