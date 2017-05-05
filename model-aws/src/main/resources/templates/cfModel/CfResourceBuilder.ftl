package ${packageName}

import com.amazonaws.AmazonWebServiceRequest
import ${modelPackage}.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.${serviceName}

<#if missingResources?size != 0>
// TODO: missing resources - <#list missingResources as resource>'${resource}'<#if resource_has_next>, </#if></#list>
</#if>

<#list resources as resource>
class ${serviceName}${resource.entityName}ResourcePropertiesBuilder : ResourcePropertiesBuilder<${resource.requestName}> {

    override val requestClazz = ${resource.requestName}::class

    <#if resource.missingProperties?size != 0>
    // TODO: missing properties - <#list resource.missingProperties as property>'${property}'<#if property_has_next>, </#if></#list>
    </#if>
    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as ${resource.requestName}).let {
            ${serviceName}.${resource.entityName}(
                <#list resource.properties as property>
                ${property.name} = request.${property.nameLC}<#if property.nonStringPrimitive>?.toString()</#if><#if property_has_next>,</#if>
                </#list>
            )
        }

}

</#list>