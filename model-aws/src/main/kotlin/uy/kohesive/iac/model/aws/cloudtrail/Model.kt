package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.codegen.model.intermediate.ListModel
import com.amazonaws.codegen.model.intermediate.MapModel
import com.amazonaws.codegen.model.intermediate.MemberModel
import com.amazonaws.codegen.model.intermediate.ShapeModel
import org.apache.commons.lang3.StringEscapeUtils

typealias RequestMap = Map<String, Any?>

data class CloudTrailEvent(
    val eventId: String,
    val eventSource: String,
    val eventName: String,
    val apiVersion: String?,
    val request: RequestMap?
)

data class RequestMapNode(
    val shape: ShapeModel? = null,
    val members: List<RequestMapNodeMember> = emptyList(),

    val constructorArgs: List<RequestMapNodeMember> = emptyList(),

    val simpleType: String? = null,
    val simpleValue: Any? = null,

    val listModel: ListModel? = null,

    val mapModel: MapModel? = null
) {
    companion object {
        fun simple(type: String, value: Any?) = RequestMapNode(
            simpleType  = type,
            simpleValue = value
        )

        fun complex(shape: ShapeModel, members: List<RequestMapNodeMember>) = RequestMapNode(
            shape   = shape,
            members = members
        )

        fun list(listModel: ListModel, items: List<RequestMapNodeMember>) = RequestMapNode(
            listModel = listModel,
            members   = items
        )

        fun map(mapModel: MapModel, items: List<RequestMapNodeMember>) = RequestMapNode(
            mapModel = mapModel,
            members  = items
        )
    }

    val simpleValueLiteral: Any? by lazy { getSimpleValueLiteral_() }

    fun isEmpty(): Boolean = ((isStructure() || isList() || isMap()) && members.isEmpty()) || (isSimple() && simpleValueLiteral == null)

    fun isList()      = listModel != null
    fun isMap()       = mapModel != null
    fun isSimple()    = simpleValue != null
    fun isStructure() = shape != null

    private fun getSimpleValueLiteral_(): Any? {
        if (!isSimple()) throw IllegalStateException("Not a simple value")

        val extractedSimpleValue = (if (simpleValue is Map<*, *>) {
            simpleValue.keys.firstOrNull()?.let { firstKey ->
                simpleValue[firstKey]
            }
        } else {
            simpleValue
        }) ?: return null

        if (extractedSimpleValue is String) {
            return "\"${StringEscapeUtils.escapeJava(extractedSimpleValue.toString())}\""
        } else if (extractedSimpleValue is Number) {
            return extractedSimpleValue.toString()
        } else if (extractedSimpleValue is Boolean) {
            return extractedSimpleValue.toString()
        } else {
            throw IllegalStateException("Unsupported simple value: ${simpleValue?.javaClass?.simpleName}")
        }
    }

}

data class RequestMapNodeMember(
    val memberModel: MemberModel,
    val value: RequestMapNode?,

    val key: String? = null // for maps
)
