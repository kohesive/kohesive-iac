package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.model.intermediate.MemberModel
import com.amazonaws.codegen.model.intermediate.OperationModel
import com.amazonaws.codegen.model.intermediate.ShapeModel

data class MemberModelKey(
    val name: String,
    val type: String
) {
    companion object {
        fun fromMemberModel(model: MemberModel) = MemberModelKey(
            name = model.getterMethodName,
            type = model.getterModel.returnType
        )
    }
}

data class CreationMethod(
    val methodName: String,
    val requestType: String,
    val resultType: String,

    val emptyResult: Boolean,

    val memberContainingCreatedEntity: String?,
    val createdEntityType: String?,
    val requestAndEntityCommonMembers: List<String>
) {

    companion object {
        fun fromOperation(model: IntermediateModel, operation: OperationModel): CreationMethod {
            val returnShape  = operation.getReturnShape(model)
            val requestShape = model.getShapeByC2jName(operation.input.variableType)

            val emptyResult = returnShape?.members?.isEmpty() ?: true

            val memberContainingCreatedEntity = returnShape?.members?.firstOrNull { member ->
                member.c2jShape.isNotEmpty() && operation.methodName.contains(member.c2jShape)
            }?.name

            // Let's find common properties in request and result entity
            val requestAndEntityCommonMembers = memberContainingCreatedEntity?.let {
                returnShape.membersAsMap[it]
            }?.let { entityMember ->
                model.getShapeByC2jName(entityMember.c2jShape)
            }?.let { entityShape ->
                val requestMembers = requestShape.members.map { MemberModelKey.fromMemberModel(it) }
                val entityMembers  = entityShape.members.map { MemberModelKey.fromMemberModel(it) }
                requestMembers.intersect(entityMembers)
            }?.map { it.name }.orEmpty()

            val some = CreationMethod(
                methodName  = operation.methodName,
                requestType = operation.input.variableType,
                resultType  = operation.returnType?.returnType ?: "Unit",

                emptyResult = emptyResult,

                memberContainingCreatedEntity = memberContainingCreatedEntity,
                requestAndEntityCommonMembers = requestAndEntityCommonMembers,

                createdEntityType = memberContainingCreatedEntity?.let {
                    returnShape.membersAsMap[it]
                }?.c2jShape
            )

            return some
        }
    }

}

fun OperationModel.getReturnShape(model: IntermediateModel): ShapeModel? = returnType?.returnType?.let { returnType ->
    model.shapes[returnType]
}