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

<#macro content classModel level=1>
<@indent level /><#if classModel.properties?size == 0>class ${classModel.simpleName}<#if classModel.isRootClass()> : ResourceProperties </#if><#else>data class ${classModel.simpleName}(
<#list classModel.properties as property>
<@indent level+1 />val ${property.name}: ${property.type}<#if property.optional>? = null</#if><#if property_has_next>,</#if>
</#list>
<@indent level />) <#if classModel.isRootClass()>: ResourceProperties </#if><#if classModel.innerClasses?size != 0>{

  <#list classModel.innerClasses as innerClass><@content innerClass level+1/></#list>
<@indent level />}</#if></#if>

</#macro>