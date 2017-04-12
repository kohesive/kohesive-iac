<#ftl encoding="UTF-8" strip_text=true>

<#function repeat string count>
    <#local result = "" />
    <#if (count > 0)>
        <#list 1..count as index>
            <#local result = result + string />
        </#list>
    </#if>
    <#return result>
</#function>

<#assign LEVEL_INDENT_STRING = "    " />

<#-- Macros -->

<#macro indent level>
    ${repeat(LEVEL_INDENT_STRING, level)}<#t>
</#macro>

<#macro structureMember memberNode level=0>
${memberNode.memberModel.setterMethodName}(<@content memberNode.value level />)
</#macro>

<#macro content requestNode level=0>
<#if requestNode.isStructure()>
${requestNode.shape.variable.simpleType}().apply {
<#list requestNode.members as memberNode>
<@indent level+1 /><@structureMember memberNode level+1 />
</#list><@indent level />}<#elseif requestNode.isSimple()>${requestNode.simpleValueLiteral}<#elseif requestNode.isMap()><#elseif requestNode.isList()></#if></#macro>