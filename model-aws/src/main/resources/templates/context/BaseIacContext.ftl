package ${contextPackageName}

<#list clientClasses as clientClass>
import ${clientClass}
</#list>

abstract class BaseIacContext() : <#list enabledClassNames as x>${x}<#if x_has_next>, </#if></#list> {



}