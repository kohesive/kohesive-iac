package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.model.intermediate.MemberModel
import com.amazonaws.codegen.model.intermediate.OperationModel
import com.amazonaws.codegen.model.intermediate.ShapeModel

class OperationHelper(operation: OperationModel, model: IntermediateModel) {

    val requestShape: ShapeModel? = model.shapes[operation.input.variableType]

    val returnShape: ShapeModel? = operation.returnType?.returnType?.let { returnType ->
        model.shapes[returnType]
    }

    val memberContainingCreatedEntityModel: MemberModel? by lazy {
        returnShape?.members?.firstOrNull { member ->
            !member.isSimple && member.c2jShape.isNotEmpty() && (
                operation.methodName.contains(member.c2jShape) || (operation.methodName + "Description").contains(member.c2jShape)
            )
        }
    }

    val entityShape: ShapeModel? by lazy {
        memberContainingCreatedEntityModel?.name.let { entityMember ->
            returnShape?.membersAsMap?.get(entityMember)
        }?.let { entityMember ->
            model.shapes[entityMember.c2jShape]
        }
    }

}
