package ${contextPackageName}

import ${metadata.packageName}.${syncInterface}
import ${metadata.packageName}.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ${serviceName}Identifiable : KohesiveIdentifiable {

}

interface ${serviceName}Enabled : ${serviceName}Identifiable {
    val ${serviceNameLC}Client: ${syncInterface}
    val ${serviceNameLC}Context: ${serviceName}Context
    fun <T> with${serviceName}Context(init: ${serviceName}Context.(${syncInterface}) -> T): T = ${serviceNameLC}Context.init(${serviceNameLC}Client)
}

open class Base${serviceName}Context(protected val context: IacContext) : ${serviceName}Enabled by context {

    <#list creationMethods as method>
    open fun ${method.methodName}(${method.nameMemberLC}: String, init: ${method.requestType}.() -> Unit): <#if method.createdEntityType??>${method.createdEntityType}<#else>${method.resultType}</#if> {
        return ${serviceNameLC}Client.${method.methodName}(${method.requestType}().apply {
            with${method.nameMember}(${method.nameMemberLC})
            init()
        })<#if method.memberContainingCreatedEntityGetter??>.${method.memberContainingCreatedEntityGetter}()</#if>
    }

    </#list>

}

<#if generateSubContext>
@DslScope
class ${serviceName}Context(context: IacContext) : Base${serviceName}Context(context) {

}
</#if>