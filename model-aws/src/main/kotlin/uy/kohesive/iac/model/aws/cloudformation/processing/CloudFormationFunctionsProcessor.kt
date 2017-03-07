package uy.kohesive.iac.model.aws.cloudformation.processing

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.cloudformation.Template
import uy.kohesive.iac.model.aws.cloudformation.functions.CFFindInMapFunction
import uy.kohesive.iac.model.aws.cloudformation.functions.CFGetAttributeFunction
import uy.kohesive.iac.model.aws.cloudformation.functions.CFJoinFunction
import uy.kohesive.iac.model.aws.cloudformation.functions.CFRefFunction
import uy.kohesive.iac.model.aws.proxy.ResourceNode
import uy.kohesive.iac.model.aws.proxy.explodeReference

fun ResourceNode.toCFJsonObject(): Any = if (this is ResourceNode.StringLiteralNode) {
    this.value()
} else if (this is ResourceNode.ImplicitNode) {
    CFRefFunction((children[0] as ResourceNode.StringLiteralNode).value().replace("-", "::"))
} else if (this is ResourceNode.MapNode) {
    CFFindInMapFunction(
        mapName        = children[0].toCFJsonObject(),
        topLevelKey    = children[1].toCFJsonObject(),
        secondLevelKey = children[2].toCFJsonObject()
    )
} else if (this is ResourceNode.RefNode) {
    CFRefFunction(children[1].toCFJsonObject())
} else if (this is ResourceNode.RefPropertyNode) {
    CFGetAttributeFunction(
        logicalNameOfResource = children[1].toCFJsonObject(),
        attributeName         = children[2].toCFJsonObject()
    )
} else if (this is ResourceNode.VariableNode) {
    CFRefFunction(children[0].toCFJsonObject())
} else {
    throw IllegalArgumentException("Unknown resource node type: ${ this::class.simpleName }")
}

/**
 * Converts the kohesive references to CloudFormation-functions-aware sub-trees.
 */
class CloudFormationFunctionsProcessor : TemplateStringProcessor {

    companion object {
        val JSON = jacksonObjectMapper() // TODO: fancy options?
    }

    override fun scope(templateNode: ObjectNode) =
        templateNode.get(Template::Resources.name) as? ObjectNode

    override fun process(context: IacContext, textNode: TextNodeInParent) {
        if (textNode.text().contains("{{kohesive")) {
            explodeReference(textNode.text()).let { explodedRefs ->
                val valueToReplaceWith = if (explodedRefs.size == 1) {
                    explodedRefs[0].toCFJsonObject()
                } else if (explodedRefs.size > 1) {
                    CFJoinFunction(explodedRefs.map { it.toCFJsonObject() })
                } else {
                    null
                }

                valueToReplaceWith?.let {
                    textNode.replaceWith(JSON.valueToTree<JsonNode>(it))
                }
            }
        }
    }

}