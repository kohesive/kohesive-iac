package uy.kohesive.iac.model.aws.proxy

fun String.isKohesiveRef() = startsWith("{{kohesive") && endsWith("}}")

data class KohesiveReference(
    val refType: String, // ref || literal-ref || var
    val targetType: String? = null,
    val targetId: String,
    val targetProperty: String? = null
) {
    companion object {
        fun fromString(str: String): KohesiveReference {
            val arr = str.drop("{{kohesive".length).dropLast("}}".length).split(':')
            if (arr.size < 3) {
                throw IllegalArgumentException("Not a valid reference: $str")
            }
            val refType = arr[1]
            if (refType == "literal-ref") {
                return KohesiveReference(refType = refType, targetId = arr[2])
            }
            if (refType == "ref") {
                if (arr.size == 4) {
                    return KohesiveReference(refType = refType, targetType = arr[2], targetId = arr[3])
                }
                if (arr.size == 5) {
                    return KohesiveReference(refType = refType, targetType = arr[2], targetId = arr[3], targetProperty = arr[4])
                }
            }
            throw IllegalArgumentException("Not a valid reference: $str")
        }
    }
}

internal fun createLiteralReference(ref: String) = "{{kohesive:literal-ref:$ref}}"

inline internal fun <reified T : Any> createReference(targetId: String)
    = "{{kohesive:ref:${T::class.java.simpleName}:$targetId}}"
inline internal fun <reified T : Any> createReference(targetId: String, property: String)
    = "{{kohesive:ref:${T::class.java.simpleName}:$targetId:$property}}"
