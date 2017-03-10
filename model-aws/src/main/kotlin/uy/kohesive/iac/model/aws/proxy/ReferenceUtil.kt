package uy.kohesive.iac.model.aws.proxy

fun String.isKohesiveRef() = startsWith("{{kohesive") && endsWith("}}")

enum class ReferenceType(val value: String) {
    Ref("ref"),
    RefProperty("ref-property"),
    Var("var"),
    Map("map"),
    Implicit("ivar");

    companion object {
        private val strValueToRefType = values().associate { it.value to it }

        fun fromString(str: String) = strValueToRefType[str] ?: throw IllegalArgumentException("Unknown ref type: $str")
    }
}

data class KohesiveReference(
    val refType: ReferenceType,

    val targetId: String,
    val targetType: String? = null,
    val targetProperty: String? = null
) {
    companion object {

        fun isReference(str: String) = try {
            fromString(str)
            true
        } catch (r: ReferenceParseException) {
            false
        }

        fun fromString(str: String): KohesiveReference {
            val arr = str.drop("{{kohesive".length).dropLast("}}".length).split(':')
            if (arr.size < 3) {
                throw ReferenceParseException("Not a valid reference: $str")
            }

            val refType = ReferenceType.fromString(arr[1])
            when (refType) {
                ReferenceType.RefProperty, ReferenceType.Map -> {
                    if (arr.size == 5) {
                        return KohesiveReference(refType = refType, targetType = arr[2], targetId = arr[3], targetProperty = arr[4])
                    } else {
                        throw ReferenceParseException("Incorrect reference $str")
                    }
                }
                ReferenceType.Ref -> {
                    if (arr.size == 4) {
                        return KohesiveReference(refType = refType, targetType = arr[2], targetId = arr[3])
                    } else {
                        throw ReferenceParseException("Incorrect reference $str")
                    }
                }
                ReferenceType.Implicit, ReferenceType.Var -> {
                    return KohesiveReference(refType = refType, targetId = arr[2])
                }
            }
        }
    }

    override fun toString() =
        "{{" + listOf("kohesive", refType.value, targetType, targetId, targetProperty).filterNotNull() + "}}"

}

class ReferenceParseException(message: String) : Exception(message)

inline internal fun <reified T : Any> createReference(targetId: String)
    = "{{kohesive:${ReferenceType.Ref.value}:${T::class.java.simpleName}:$targetId}}"
inline internal fun <reified T : Any> createReference(targetId: String, property: String)
    = "{{kohesive:${ReferenceType.RefProperty.value}:${T::class.java.simpleName}:$targetId:$property}}"
