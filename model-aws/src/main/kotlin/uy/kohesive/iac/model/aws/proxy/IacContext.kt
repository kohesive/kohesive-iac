package uy.kohesive.iac.model.aws.proxy

import com.amazonaws.services.ec2.AmazonEC2
import uy.kohesive.iac.model.aws.DeferredAmazonEC2
import uy.kohesive.iac.model.aws.ParameterizedValue
import java.util.*

class IacContext(
    val environment: String,
    val planId: String,
    val init: IacContext.()->Unit = {}
) {

    private val objectsToIds = IdentityHashMap<Any, String>()
    private val variables: MutableMap<String, ParameterizedValue> = hashMapOf()

    val ec2Client: AmazonEC2 by lazy { DeferredAmazonEC2(this) }

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

    fun build(builder: IacContext.()->Unit) {
        this.builder()
    }
}