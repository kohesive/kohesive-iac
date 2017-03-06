package uy.kohesive.iac.model.aws.cloudformation

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import uy.kohesive.iac.model.aws.IacContext

/**
 * Replaces the negative integers from context with string {{kohesive:var:*}} references.
 */
class NumericVarPreProcessor : TemplateStringProcessor {

    override fun scope(templateNode: ObjectNode) =
        templateNode.get(Template::Resources.name) as? ObjectNode

    override fun process(context: IacContext, textNode: TextNodeInParent) {
        var changed = false
        var text    = textNode.text()
        context.numericVarTracker.numericToVarMap.forEach { numeric, variable ->
            if (text.contains(numeric.toString())) {
                text = text.replace(numeric.toString(), variable.ref)
                changed = true
            }
        }
        if (changed) {
            textNode.replaceWith(TextNode(text))
        }
    }

}