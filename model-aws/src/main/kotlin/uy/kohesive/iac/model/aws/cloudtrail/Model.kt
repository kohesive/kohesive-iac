package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.codegen.model.intermediate.ListModel
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
    val shape: ShapeModel?,
    val members: List<RequestMapNodeMember>,

    val simpleType: String?,
    val simpleValue: Any?,

    val listModel: ListModel? = null
) {
    companion object {
        fun simple(type: String, value: Any?) = RequestMapNode(
            shape       = null,
            members     = emptyList(),
            simpleType  = type,
            simpleValue = value
        )

        fun complex(shape: ShapeModel, members: List<RequestMapNodeMember>) = RequestMapNode(
            shape       = shape,
            members     = members,
            simpleType  = null,
            simpleValue = null
        )

        fun list(listModel: ListModel, items: List<RequestMapNodeMember>) = RequestMapNode(
            listModel   = listModel,
            shape       = null,
            members     = items,
            simpleType  = null,
            simpleValue = null
        )
    }

    fun isList()      = listModel != null
    fun isSimple()    = simpleType != null
    fun isStructure() = shape != null

}

data class ApiCallData(
    val shape: ShapeModel,
    val requestMap: RequestMap
)

data class RequestMapNodeMember(
    val memberModel: MemberModel,
    val value: RequestMapNode?
)
