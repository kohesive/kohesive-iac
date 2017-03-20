package ${targetClientPackageName}

import ${awsClientPackageName}.Abstract${syncInterface}
import ${awsClientPackageName}.${syncInterface}
import ${awsClientPackageName}.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class ${baseDeferredClientClassName}(val context: IacContext) : Abstract${syncInterface}(), ${syncInterface} {

    <#list creationMethods as method>
    override fun ${method.methodName}(request: ${method.requestType}): ${method.resultType} {
        return with (context) {
            request.registerWithAutoName()
            <#if method.emptyResult>
            ${method.resultType}().registerWithSameNameAs(request)
            <#elseif method.memberContainingCreatedEntity??>
            ${method.resultType}().with${method.createdEntityType}(
                makeProxy<${method.requestType}, ${method.createdEntityType}>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        <#list method.requestAndEntityCommonMembers as member>
                        ${method.requestType}::${member} to ${method.createdEntityType}::${member}<#if member_has_next>,</#if>
                        </#list>
                    )
                )
            )
            <#else>
            ${method.resultType}().registerWithSameNameAs(request) // TODO: check for wiring members to request
            </#if>
        }
    }

    </#list>

}

<#if generateSubClient>
class ${deferredClientClassName}(context: IacContext) : ${baseDeferredClientClassName}(context)
</#if>