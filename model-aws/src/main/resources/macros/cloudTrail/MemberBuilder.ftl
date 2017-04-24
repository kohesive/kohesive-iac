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

<#macro listMacro listRequestNode level=0><#if !listRequestNode.vararg>listOf(</#if><#list listRequestNode.members as memberNode>
<@indent level /><@content memberNode.value level /><#if memberNode_has_next>,</#if></#list>
<@indent level-1 /><#if !listRequestNode.vararg>)</#if></#macro>

<#macro mapMacro mapRequestNode level=0>mapOf(<#list mapRequestNode.members as memberNode>
<@indent level />"${memberNode.key}" to <@content memberNode.value level /><#if memberNode_has_next>,</#if></#list>
<@indent level-1 />)</#macro>

<#macro enumMacro mapRequestNode level=0>${mapRequestNode.shape.c2jName}.${mapRequestNode.enumValue}</#macro>

<#macro dateMacro dateValue>Calendar.getInstance().apply { set(${dateValue.year?c}, ${dateValue.month}, ${dateValue.date}, ${dateValue.hrs}, ${dateValue.min}) }.time</#macro>

<#macro content requestNode level=0>
<#if requestNode.isStructure()>
${requestNode.shape.variable.simpleType}(<#list requestNode.constructorArgs as arg><@content arg.value level /><#if arg_has_next>, </#if></#list>)<#if requestNode.members?size != 0>.apply {
<#list requestNode.members as memberNode>
<@indent level+1 /><@structureMember memberNode level+1 />
</#list><@indent level />}</#if><#elseif requestNode.isDate()><@dateMacro requestNode.dateValue /><#elseif requestNode.isSimple()>${requestNode.simpleValueLiteral}<#elseif requestNode.isEnum()><@enumMacro requestNode level+1 /><#elseif requestNode.isMap()><@mapMacro requestNode level+1 /><#elseif requestNode.isList()><@listMacro requestNode level+1 /></#if></#macro>