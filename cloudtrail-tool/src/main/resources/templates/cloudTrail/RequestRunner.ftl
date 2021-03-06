package ${packageName}

<#list imports as import>
import ${import}
</#list>

// Generated from CloudTrail event ${event.eventId} received on ${event.dateFormatted}
class ${className}(val client: ${awsModel.metadata.syncInterface}) {

    fun run() {
        val request = <@CloudTrailMemberMacro.content requestNode 2/>

        client.${operation.getMethodName()}(request)
    }

}