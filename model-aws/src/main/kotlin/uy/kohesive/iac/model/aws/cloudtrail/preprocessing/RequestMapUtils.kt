package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

fun RequestMap?.rename(keyFrom: String, keyTo: String): RequestMap? = if (this == null) {
    null
} else {
    this - keyFrom + this[keyFrom].toMapValue(keyTo)
}

fun RequestMap?.mapByPath(path: String): RequestMap? = byPath(path) as? RequestMap

fun RequestMap?.byPath(path: String): Any? = if (this == null) {
    null
} else {
    path.split('.').fold<String, Any?>(this) { currentObj, subPath ->
        if (currentObj == null) {
            null
        } else {
            (currentObj as? RequestMap)?.get(subPath)
        }
    }
}

fun Any?.toMapValue(name: String): Map<String, Any> {
    if (this == null) {
        return emptyMap()
    } else {
        return mapOf(name to this)
    }
}

fun RequestMap?.replaceAndTransform(keyFrom: String, keyTo: String, transform: (RequestMap) -> RequestMap?): RequestMap? = if (this == null) {
    null
} else {
    this - keyFrom + this[keyFrom]?.let {
        transform(it as RequestMap)
    }.toMapValue(keyTo)
}