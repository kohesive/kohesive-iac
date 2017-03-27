<#ftl strip_whitespace="no" />
<#macro content classModel>
data class ${classModel.simpleName}() {
  <#list classModel.innerClasses as innerClass>
    <@content innerClass />
  </#list>
}

</#macro>