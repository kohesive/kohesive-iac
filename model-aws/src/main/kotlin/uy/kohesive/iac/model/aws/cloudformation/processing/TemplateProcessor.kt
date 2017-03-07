package uy.kohesive.iac.model.aws.cloudformation.processing

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ContainerNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import uy.kohesive.iac.model.aws.IacContext

interface TemplateStringProcessor {
    /**
     * Override to limit processing scope.
     * If this method returns null, no preprocessing will be performed.
     */
    fun scope(templateNode: ObjectNode): ObjectNode? = templateNode
    fun process(context: IacContext, textNode: TextNodeInParent)
}

class TemplateProcessor(val context: IacContext) {

    companion object {
        val stringProcessors: List<TemplateStringProcessor> = listOf(
           NumericVarPreProcessor(),
            CloudFormationFunctionsProcessor()
        )
    }

    fun process(templateNode: ObjectNode) {
        stringProcessors.forEach { preprocessor ->
            preprocessor.scope(templateNode)?.let { scope ->
                ArrayList<TextNodeInParent>().apply {
                    scope.collectTextNodes(this)
                }.forEach { textNode ->
                    preprocessor.process(context, textNode)
                }
            }
        }
    }

}

interface TextNodeInParent {
    val textNode: TextNode
    fun replaceWith(node: JsonNode)
    fun text() = textNode.textValue()
}

data class TextNodeInArray(
        override val textNode: TextNode,
        val array: ArrayNode,
        val index: Int
) : TextNodeInParent {

    override fun replaceWith(node: JsonNode) {
        array.set(index, node)
    }

}

data class TextNodeInObject(
        override val textNode: TextNode,
        val obj: ObjectNode,
        val fieldName: String
) : TextNodeInParent {

    override fun replaceWith(node: JsonNode) {
        obj.replace(fieldName, node)
    }

}

fun ContainerNode<*>.collectTextNodes(textNodes: MutableList<TextNodeInParent>) {
    if (this is ArrayNode) {
        forEachIndexed { index, child ->
            if (child is ContainerNode<*>) {
                child.collectTextNodes(textNodes)
            } else if (child is TextNode) {
                textNodes.add(TextNodeInArray(
                    textNode = child,
                    array    = this,
                    index    = index
                ))
            }
        }
    } else if (this is ObjectNode) {
        fields().forEach { entry ->
            val fieldName = entry.key
            val value     = entry.value

            if (value is ContainerNode<*>) {
                value.collectTextNodes(textNodes)
            } else if (value is TextNode) {
                textNodes.add(TextNodeInObject(
                    textNode  = value,
                    obj       = this,
                    fieldName = fieldName
                ))
            }
        }
    }
}