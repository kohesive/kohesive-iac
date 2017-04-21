package uy.kohesive.iac.model.aws.codegen

import freemarker.template.Template

enum class TemplateDescriptor(
    val location: String,
    val childTemplates: List<ChildTemplate> = emptyList()
) {

    BaseIacContext("/templates/context/IacContext.ftl"),
    ServiceContext("/templates/context/ServiceContext.ftl"),
    DeferredClient("/templates/client/DeferredClient.ftl"),
    CloudFormationModel("/templates/cfModel/CfModel.ftl", listOf(
        ChildTemplate(
            templateLocation  = "/macros/cfModel/ModelClass.ftl",
            importAsNamespace = "CFModelClassMacro"
        )
    )),
    RequestBuilder("/templates/cloudTrail/RequestBuilder.ftl", listOf(
        ChildTemplate(
            templateLocation  = "/macros/cloudTrail/MemberBuilder.ftl",
            importAsNamespace = "CloudTrailMemberMacro"
        )
    )),
    RequestRunner("/templates/cloudTrail/RequestRunner.ftl", listOf(
        ChildTemplate(
            templateLocation  = "/macros/cloudTrail/MemberBuilder.ftl",
            importAsNamespace = "CloudTrailMemberMacro"
        )
    ));

    fun load(): Template = TemplateLoader.getTemplate(this)

}

data class ChildTemplate(
    val templateLocation: String,
    val importAsNamespace: String
)