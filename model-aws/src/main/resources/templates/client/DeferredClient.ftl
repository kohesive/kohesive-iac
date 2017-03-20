package ${targetClientPackageName}

import ${awsClientPackageName}.Abstract${syncInterface}
import ${awsClientPackageName}.${syncInterface}
import ${awsClientPackageName}.model.*
import uy.kohesive.iac.model.aws.IacContext

open class ${baseDeferredClientClassName}(val context: IacContext) : Abstract${syncInterface}(), ${syncInterface} {

    <#list creationMethods as method>
    override fun ${method.methodName}(request: ${method.requestType}): ${method.resultType} {
        return with (context) {
            request.registerWithAutoName()
            <#if method.emptyResult>
            ${method.resultType}().registerWithSameNameAs(request)
            </#if>
        }
    }
    </#list>

}

<#if generateSubClient>
class ${deferredClientClassName}(context: IacContext) : ${baseDeferredClientClassName}(context)
</#if>