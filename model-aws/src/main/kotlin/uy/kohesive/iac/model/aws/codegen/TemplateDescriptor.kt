package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.model.config.templates.ChildTemplate
import freemarker.template.Template

enum class TemplateDescriptor(
    val location: String,
    val childTemplates: List<ChildTemplate> = emptyList()
) {

    BaseIacContext("/templates/context/BaseIacContext.ftl"),
    ServiceContext("/templates/context/ServiceContext.ftl"),
    DeferredClient("/templates/client/DeferredClient.ftl");

    fun load(): Template = TemplateLoader.getTemplate(this)

}

data class ChildTemplate(
    val location: String,
    val importAsNamespace: String
)