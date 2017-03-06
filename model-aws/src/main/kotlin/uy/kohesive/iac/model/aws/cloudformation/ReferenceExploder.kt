package uy.kohesive.iac.model.aws.cloudformation

import uy.kohesive.iac.model.aws.proxy.ReferenceType
import java.util.*
import kotlin.collections.ArrayList

fun explodeReference(str: String): List<ResourceNode> {
    val stack = Stack<ResourceNode>()

    fun isWithinUnresolvedReference()
        = stack.filter { it.isReferenceNode() }.any { it.isUnresolved() }

    var i = 0
    while (i < str.length) {
        val currentNode: ResourceNode? = if (stack.empty()) null else stack.peek()
        val nextRefStart = str.indexOf("{{kohesive:", i)
        val nextRefEnd   = str.indexOf("}}", i)

        // Start new reference
        if (nextRefStart == i) {
            // We need to figure out the type of reference

            val refTypeStart = nextRefStart + "{{kohesive:".length
            val refTypeEnd   = str.indexOf(":", refTypeStart)

            val refType = ReferenceType.fromString(str.substring(
                startIndex = refTypeStart,
                endIndex   = refTypeEnd
            ))

            // Push the new reference node to the stack
            val referenceNode = ResourceNode.forReferenceType(refType)
            stack.push(referenceNode)

            i = refTypeEnd + 1
        } else if (nextRefEnd == i && isWithinUnresolvedReference()) {
            // Pop the arguments (reference parts) and assign them as reference children

            val referenceArguments = ArrayList<ResourceNode>()
            while (true) {
                val node = stack.peek()

                if (node.isReferenceNode() && node.isUnresolved()) {
                    for (referenceNode in referenceArguments) {
                        node.insertChild(referenceNode)
                    }
                    break
                } else {
                    referenceArguments.add(stack.pop())
                }
            }

            i += 2
        } else {
            if (str[i] == ':' && isWithinUnresolvedReference()) {
                if (i != nextRefStart - 1) {
                    stack.push(ResourceNode.StringLiteralNode())
                }
                i++; continue
            }

            // Must be a string literal
            val stringNode = currentNode as? ResourceNode.StringLiteralNode ?:
                ResourceNode.StringLiteralNode().apply {
                    stack.push(this)
                }

            stringNode.append(str[i++])
        }
    }

    return stack
}

sealed class ResourceNode(
    val arity: Int
) {

    companion object {
        fun forReferenceType(refType: ReferenceType) = when (refType) {
            ReferenceType.Ref         -> RefNode()
            ReferenceType.RefProperty -> RefPropertyNode()
            ReferenceType.Var         -> VariableNode()
            ReferenceType.Map         -> MapNode()
            ReferenceType.Implicit    -> ImplicitNode()
        }
    }

    class StringLiteralNode : ResourceNode(0) {
        private val value: StringBuffer = StringBuffer()
        fun append(c: Char) = value.append(c)
        fun value() = value.toString()
        override fun toString() = "\"$value\""
    }
    class ImplicitNode    : ResourceNode(1)
    class VariableNode    : ResourceNode(1)
    class RefNode         : ResourceNode(2)
    class RefPropertyNode : ResourceNode(3)
    class MapNode         : ResourceNode(3)

    val children = ArrayList<ResourceNode>()

    fun isReferenceNode() = arity > 0
    fun isUnresolved()    = children.size < arity

    fun insertChild(childNode: ResourceNode, index: Int = 0) {
        if (children.size + 1 > arity) {
            throw IllegalStateException("Max children count reached for ${ this::class.simpleName }")
        }
        children.add(index, childNode)
    }

    override fun toString() = "${ this::class.simpleName }: (${children.joinToString(", ")})"
}

