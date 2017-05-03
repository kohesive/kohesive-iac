package ${packageName}

object CFResources {

    val serviceResourceContainers = listOf(
<#list serviceNames as serviceName>
        ${serviceName}::class<#if serviceName_has_next>,</#if>
</#list>
    )

}