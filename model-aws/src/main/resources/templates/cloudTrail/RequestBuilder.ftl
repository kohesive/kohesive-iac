<#list shape.members as memberModel>
    <#if memberModel.http.isHeader() >
          <#if memberModel.variable.simpleType == "Date">
              ${shape.variable.variableName}.${memberModel.setterMethodName}(com.amazonaws.util.DateUtils.parseRFC822Date(context.readText()));
          <#else>
              ${shape.variable.variableName}.${memberModel.setterMethodName}(<@MemberUnmarshallerDeclarationMacro.content memberModel />.unmarshall(context));
          </#if>
    </#if>
</#list>