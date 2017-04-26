package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.codegen.model.intermediate.ListModel
import com.amazonaws.codegen.model.intermediate.MapModel
import com.amazonaws.codegen.model.intermediate.MemberModel
import com.amazonaws.codegen.model.intermediate.ShapeModel
import org.apache.commons.lang3.StringEscapeUtils
import uy.kohesive.iac.model.aws.cloudtrail.utils.DateTime

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
    val members: MutableList<RequestMapNodeMember> = mutableListOf(),

    val constructorArgs: MutableList<RequestMapNodeMember> = mutableListOf(),

    var simpleType: String? = null,
    var simpleValue: Any? = null,

    var listModel: ListModel? = null,
    var vararg: Boolean = false,

    val mapModel: MapModel? = null,

    var enumValue: String? = null,

    val dateValue: DateTime? = null
) {
    companion object {
        fun simple(type: String, value: Any?) = RequestMapNode(
            simpleType  = type,
            simpleValue = value
        )

        fun date(dateValue: String) = RequestMapNode(
            dateValue = DateTime.parse(dateValue)
        )

        fun complex(shape: ShapeModel, members: List<RequestMapNodeMember>) = RequestMapNode(
            shape   = shape,
            members = members.toMutableList()
        )

        fun list(listModel: ListModel, items: List<RequestMapNodeMember>) = RequestMapNode(
            listModel = listModel,
            members   = items.toMutableList()
        )

        fun map(mapModel: MapModel, items: List<RequestMapNodeMember>) = RequestMapNode(
            mapModel = mapModel,
            members  = items.toMutableList()
        )

        fun enum(enumValue: String, shape: ShapeModel) = RequestMapNode(
            shape     = shape,
            enumValue = enumValue
        )
    }

    val simpleValueLiteral: Any? by lazy { getSimpleValueLiteral_() }

    fun isEmpty(): Boolean =
        ((isStructure() || isList() || isMap()) && members.isEmpty()) ||
        (isSimple() && simpleValueLiteral == null) ||
        (isEnum() && enumValue == null)

    fun isEnum()      = enumValue != null
    fun isList()      = listModel != null
    fun isMap()       = mapModel != null
    fun isSimple()    = simpleValue != null
    fun isStructure() = shape != null && enumValue == null
    fun isDate()      = dateValue != null

    private fun getSimpleValueLiteral_(): Any? {
        if (!isSimple()) throw IllegalStateException("Not a simple value")

        val capturedSimpleValue  = simpleValue
        val extractedSimpleValue = (if (capturedSimpleValue is Map<*, *>) {
            capturedSimpleValue.keys.firstOrNull()?.let { firstKey ->
                capturedSimpleValue[firstKey]
            }
        } else {
            capturedSimpleValue
        }) ?: return null

        if (simpleType == "Integer") {
            if (simpleValue is Int) {
                return simpleValue
            } else {
                return simpleValue?.toString()?.toInt()
            }
        } else if (simpleType == "Long") {
            if (simpleValue is Long) {
                return simpleValue
            } else {
                return simpleValue?.toString()?.toLong()
            }
        }

        if (extractedSimpleValue is String) {
            return "\"${StringEscapeUtils.escapeJava(extractedSimpleValue.toString())}\"".replace("$", "\\$")
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
    var memberModel: MemberModel,
    val value: RequestMapNode?,

    val key: String? = null // for maps
)
