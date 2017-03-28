package ${packageName}

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object ${serviceName} {

<#list classes as classModel>
<@CFModelClassMacro.content classModel />
</#list>

}