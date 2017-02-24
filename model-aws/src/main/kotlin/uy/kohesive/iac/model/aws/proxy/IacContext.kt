package uy.kohesive.iac.model.aws.proxy

import java.util.*

class IacContext(
    val environment: String,
    val planId: String
) {

    private val objectsToIds = IdentityHashMap<Any, String>()

    fun getId(obj: Any): String?  = objectsToIds[obj]

    fun <T : Any> T.withId(id: String): T = apply {
        objectsToIds[this@withId] = id
    }

}