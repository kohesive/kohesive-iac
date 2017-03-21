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
    val requestAndEntityCommonMembers: List<String>,
    val requestAndResponseCommonMembers: List<String>
) {

    companion object {
        fun fromOperation(model: IntermediateModel, operation: OperationModel): CreationMethod {
            fun commonMemberNames(shapeOne: ShapeModel?, shapeTwo: ShapeModel?): List<String> {
                if (shapeOne == null || shapeTwo == null) {
                    return emptyList()
                }

                val shapeOneMembers = shapeOne.members?.map { MemberModelKey.fromMemberModel(it) }.orEmpty()
                val shapeTwoMembers = shapeTwo.members?.map { MemberModelKey.fromMemberModel(it) }.orEmpty()
                return shapeOneMembers.intersect(shapeTwoMembers).map { it.name }
            }

            val returnShape: ShapeModel?  = operation.getReturnShape(model)
            val requestShape: ShapeModel? = model.shapes[operation.input.variableType]

            val emptyResult = returnShape?.members?.isEmpty() ?: true

            val memberContainingCreatedEntity = returnShape?.members?.firstOrNull { member ->
                member.c2jShape.isNotEmpty() && (
                    operation.methodName.contains(member.c2jShape) || (operation.methodName + "Description").contains(member.c2jShape)
                )
            }?.name

            val entityShape = memberContainingCreatedEntity?.let {
                returnShape.membersAsMap[it]
            }?.let { entityMember ->
                model.getShapeByC2jName(entityMember.c2jShape)
            }

            val requestAndEntityCommonMembers   = entityShape?.let { commonMemberNames(it, requestShape) }.orEmpty()
            val requestAndResponseCommonMembers = returnShape?.let { commonMemberNames(it, requestShape) }.orEmpty()

            return CreationMethod(
                methodName  = operation.methodName,
                requestType = operation.input.variableType,
                resultType  = operation.returnType?.returnType ?: "Unit",

                emptyResult = emptyResult,

                memberContainingCreatedEntity   = memberContainingCreatedEntity,
                requestAndEntityCommonMembers   = requestAndEntityCommonMembers,
                requestAndResponseCommonMembers = requestAndResponseCommonMembers,

                createdEntityType = memberContainingCreatedEntity?.let {
                    returnShape.membersAsMap[it]
                }?.c2jShape
            )
        }
    }

}

fun OperationModel.getReturnShape(model: IntermediateModel): ShapeModel? = returnType?.returnType?.let { returnType ->
    model.shapes[returnType]
}