package ${packageName}

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties

object ${serviceName} {

<#list classes as classModel>
<@CFModelClassMacro.content classModel />
</#list>

}