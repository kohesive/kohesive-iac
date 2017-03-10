package uy.kohesive.iac.model.aws.cloudformation.processing

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.cloudformation.Template
import uy.kohesive.iac.model.aws.cloudformation.functions.CFBase64Function

class UserDataBase64Processor : TemplateStringProcessor {

    companion object {
        val JSON = jacksonObjectMapper() // TODO: fancy options?
    }

    override fun scope(templateNode: ObjectNode) =
        listOf(templateNode.get(Template::Resources.name) as? ObjectNode).filterNotNull()

    override fun process(context: IacContext, textNode: TextNodeInParent) {
        (textNode as? TextNodeInObject)?.let { textNodeInObject ->
            if (textNodeInObject.fieldName == "UserData") {
                textNode.replaceWith(JSON.valueToTree<ObjectNode>(CFBase64Function(textNode.text())))
            }
        }
    }
}