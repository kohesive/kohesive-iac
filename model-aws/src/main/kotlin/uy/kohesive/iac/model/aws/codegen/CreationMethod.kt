package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.model.intermediate.OperationModel
import com.amazonaws.codegen.model.intermediate.ShapeModel

data class CreationMethod(
    val methodName: String,
    val requestType: String,
    val resultType: String,

    val emptyResult: Boolean
) {

    companion object {
        fun fromOperation(model: IntermediateModel, it: OperationModel): CreationMethod {
            val emptyResult = it.getReturnShape(model)?.members?.isEmpty() ?: true

            return CreationMethod(
                methodName  = it.methodName,
                requestType = it.input.variableType,
                resultType  = it.returnType?.returnType ?: "Unit",

                emptyResult = emptyResult
            )
        }
    }

}

fun OperationModel.getReturnShape(model: IntermediateModel): ShapeModel? = returnType?.returnType?.let { returnType ->
    model.shapes[returnType]
}