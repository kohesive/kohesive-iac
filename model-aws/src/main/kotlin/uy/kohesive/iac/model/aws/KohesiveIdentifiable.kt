package uy.kohesive.iac.model.aws

import com.amazonaws.AmazonWebServiceRequest
import java.util.*

interface KohesiveIdentifiable {

    val objectsToNames: IdentityHashMap<Any, String>

    fun getName(obj: Any) = objectsToNames[obj]
    fun getNameStrict(obj: Any) = objectsToNames[obj] ?: throw IllegalStateException("Unknown object $obj")

    fun <T : Any> T.registerWithName(name: String): T = apply {
        objectsToNames[this@registerWithName] = name
    }

    fun <T : Any> T.registerWithSameNameAs(another: Any): T = apply {
        objectsToNames[this@registerWithSameNameAs] = getNameStrict(another)
    }

    fun <T : AmazonWebServiceRequest> T.registerWithAutoName(): T = apply {
        registerWithName(AutoNaming.getName(this) ?: throw IllegalArgumentException("Unknown request"))
    }

}

interface TagAware<out T : Any> {
    fun createTag(key: String, value: String): T
    fun tags(vararg keysToValues: Pair<String, String>): List<T> =
        keysToValues.map { createTag(it.first, it.second) }
}