package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.model.intermediate.MemberModel
import com.amazonaws.codegen.model.intermediate.OperationModel
import com.amazonaws.codegen.model.intermediate.ShapeModel
import uy.kohesive.iac.model.aws.utils.firstLetterToLowerCase

data class CreationMethod(
        val methodName: String,
        val requestType: String,
        val resultType: String,

        val emptyResult: Boolean,

        val memberContainingCreatedEntity: String?,
        val memberContainingCreatedEntityGetter: String?,
        val memberContainingCreatedEntityFluent: String?,
        val createdEntityType: String?,
        val requestAndEntityCommonMembers: List<String>,
        val requestAndResponseCommonMembers: List<String>,

        val nameMember: String?,
        val nameMemberLC: String?
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

            val operationHelper = OperationHelper(operation, model)

            val requestAndEntityCommonMembers   = operationHelper.entityShape?.let { commonMemberNames(it, operationHelper.requestShape) }.orEmpty()
            val requestAndResponseCommonMembers = operationHelper.returnShape?.let { commonMemberNames(it, operationHelper.requestShape) }.orEmpty()

            val nameMemberName = operationHelper.requestShape?.let { requestShape ->
                requestShape.membersAsMap["Name"] ?: requestShape.membersAsMap[requestShape.shapeName.replace("Create", "").replace("Request", "") + "Name"]
            }?.name

            return CreationMethod(
                methodName  = operation.methodName,
                requestType = operation.input.variableType,
                resultType  = operation.returnType?.returnType ?: "Unit",

                emptyResult = operationHelper.returnShape?.members?.isEmpty() ?: true,

                memberContainingCreatedEntity       = operationHelper.memberContainingCreatedEntityModel?.name,
                memberContainingCreatedEntityGetter = operationHelper.memberContainingCreatedEntityModel?.getterMethodName,
                memberContainingCreatedEntityFluent = operationHelper.memberContainingCreatedEntityModel?.fluentSetterMethodName,
                requestAndEntityCommonMembers       = requestAndEntityCommonMembers,
                requestAndResponseCommonMembers     = requestAndResponseCommonMembers,

                createdEntityType  = operationHelper.entityShape?.c2jName,

                nameMember   = nameMemberName,
                nameMemberLC = nameMemberName?.firstLetterToLowerCase()
            )
        }

    }

}

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


