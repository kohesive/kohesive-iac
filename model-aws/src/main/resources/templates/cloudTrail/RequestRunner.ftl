package ${packageName}

<#list imports as import>
import ${import}
</#list>

class ${className}(val client: ${awsModel.metadata.syncInterface}) {

    fun run() {
        val request = <@CloudTrailMemberMacro.content requestNode 2/>

        client.${operation.getMethodName()}(request)
    }

}