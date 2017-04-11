package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.codegen.model.intermediate.ListModel
import com.amazonaws.codegen.model.intermediate.MapModel
import com.amazonaws.codegen.model.intermediate.MemberModel
import com.amazonaws.codegen.model.intermediate.ShapeModel

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
            shape       = shape,
            members     = members
        )

        fun list(listModel: ListModel, items: List<RequestMapNodeMember>) = RequestMapNode(
            listModel   = listModel,
            members     = items
        )

        fun map(mapModel: MapModel, items: List<RequestMapNodeMember>) = RequestMapNode(
            mapModel = mapModel,
            members  = items
        )
    }

    fun isList()      = listModel != null
    fun isMap()       = mapModel != null
    fun isSimple()    = simpleType != null
    fun isStructure() = shape != null

}

data class ApiCallData(
    val shape: ShapeModel,
    val requestMap: RequestMap
)

data class RequestMapNodeMember(
    val memberModel: MemberModel,
    val value: RequestMapNode?,

    val key: String? = null // for maps
)
