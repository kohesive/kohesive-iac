package uy.kohesive.iac.model.aws.template

import junit.framework.TestCase
import uy.kohesive.iac.model.aws.proxy.ResourceNode
import uy.kohesive.iac.model.aws.proxy.explodeReference

class ReferenceExploderTest : TestCase() {

    fun testMap() {
        val exploded: List<ResourceNode> = explodeReference("{{kohesive:map:AWSRegionArch2AMI:{{kohesive:ivar:AWS-Region}}:{{kohesive:map:AWSInstanceType2Arch:{{kohesive:var:InstanceType}}:Arch}}}}")
        assertEquals(1, exploded.size)
        assertTrue(exploded[0] is ResourceNode.MapNode)

        val outerMap = exploded[0] as ResourceNode.MapNode

        assertTrue(outerMap.children[0] is ResourceNode.StringLiteralNode)
        assertTrue(outerMap.children[1] is ResourceNode.ImplicitNode)
        assertTrue(outerMap.children[2] is ResourceNode.MapNode)

        assertEquals("AWSRegionArch2AMI", (outerMap.children[0] as ResourceNode.StringLiteralNode).value())
        assertEquals("AWS-Region", ((outerMap.children[1] as ResourceNode.ImplicitNode).children[0] as ResourceNode.StringLiteralNode).value())

        val innerMap = outerMap.children[2] as ResourceNode.MapNode
        assertTrue(innerMap.children[0] is ResourceNode.StringLiteralNode)
        assertTrue(innerMap.children[1] is ResourceNode.VariableNode)
        assertTrue(innerMap.children[2] is ResourceNode.StringLiteralNode)

        assertEquals("AWSInstanceType2Arch", (innerMap.children[0] as ResourceNode.StringLiteralNode).value())
        assertEquals("Arch", (innerMap.children[2] as ResourceNode.StringLiteralNode).value())

        val instanceTypeVar = innerMap.children[1] as ResourceNode.VariableNode
        assertEquals(1, instanceTypeVar.children.size)
        assertTrue(instanceTypeVar.children[0] is ResourceNode.StringLiteralNode)
        assertEquals("InstanceType", (instanceTypeVar.children[0] as ResourceNode.StringLiteralNode).value())
    }

    fun testPropertyRef() {
        val exploded: List<ResourceNode> = explodeReference("{{kohesive:ref-property:InstanceProfile:ElasticsearchInstanceProfile:Arn}}")
        assertEquals(1, exploded.size)
        assertTrue(exploded[0] is ResourceNode.RefPropertyNode)

        val propertyRefNode = exploded[0] as ResourceNode.RefPropertyNode
        assertEquals(3, propertyRefNode.children.size)
        assertTrue(propertyRefNode.children.all { it is ResourceNode.StringLiteralNode })

        assertEquals(
            listOf("InstanceProfile", "ElasticsearchInstanceProfile", "Arn"),
            propertyRefNode.children.filterIsInstance<ResourceNode.StringLiteralNode>().map(ResourceNode.StringLiteralNode::value)
        )
    }

    fun testSimpleVar() {
        val exploded: List<ResourceNode> = explodeReference("{{kohesive:var:KeyName}}")
        assertEquals(1, exploded.size)
        assertTrue(exploded[0] is ResourceNode.VariableNode)

        val varNode = exploded[0] as ResourceNode.VariableNode
        assertFalse(varNode.isUnresolved())
        assertEquals(1, varNode.children.size)
        assertTrue(varNode.children[0] is ResourceNode.StringLiteralNode)

        val literal = varNode.children[0] as ResourceNode.StringLiteralNode
        assertEquals("KeyName", literal.value())
    }

    fun testSimpleVarAndText() {
        val exploded: List<ResourceNode> = explodeReference("Hello, {{kohesive:var:KeyName}}!")
        assertEquals(3, exploded.size)
        assertTrue(exploded[0] is ResourceNode.StringLiteralNode)
        assertTrue(exploded[1] is ResourceNode.VariableNode)
        assertTrue(exploded[2] is ResourceNode.StringLiteralNode)

        val helloLiteralNode = exploded[0] as ResourceNode.StringLiteralNode
        assertEquals("Hello, ", helloLiteralNode.value())
        val exclamationLiteralNode = exploded[2] as ResourceNode.StringLiteralNode
        assertEquals("!", exclamationLiteralNode.value())

        val varNode = exploded[1] as ResourceNode.VariableNode
        assertFalse(varNode.isUnresolved())
        assertEquals(1, varNode.children.size)
        assertTrue(varNode.children[0] is ResourceNode.StringLiteralNode)

        val keyNameLiteral = varNode.children[0] as ResourceNode.StringLiteralNode
        assertEquals("KeyName", keyNameLiteral.value())
    }

}