package uy.kohesive.iac.model.aws

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import java.util.*

interface KohesiveIdentifiable {
    val objectsToIds: IdentityHashMap<Any, String>

    fun getId(obj: Any): String?  = objectsToIds[obj]

    fun <T : AmazonWebServiceRequest> T.withKohesiveId(id: String): T = apply {
        objectsToIds[this@withKohesiveId] = id
    }

    fun <K: ResponseMetadata, T : AmazonWebServiceResult<out K>> T.withKohesiveId(id: String): T = apply {
        objectsToIds[this@withKohesiveId] = id
    }

    // TODO: any way to avoid Any?  what types are possible here?
    fun <T : Any> T.withKohesiveId(id: String): T = apply {
        objectsToIds[this@withKohesiveId] = id
    }
}

interface TagAware<out T : Any> {
    fun createTag(key: String, value: String): T
    fun tags(vararg keysToValues: Pair<String, String>): List<T> =
        keysToValues.map { createTag(it.first, it.second) }
}