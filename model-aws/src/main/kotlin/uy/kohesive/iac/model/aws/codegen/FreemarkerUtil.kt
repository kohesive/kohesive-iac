package uy.kohesive.iac.model.aws.codegen

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler

class TemplateLoader {

    fun getTemplate(templateName: String): Template {
        // Create a new FreeMarker config for each top-level template, so that
        // they don't share the same macro namespace
        val fmConfig = newFreeMarkerConfig()

        // TODO: implement
        // Common child templates
//        importChildTemplates(fmConfig, templateConfig.getCommonChildTemplates())
        // Child templates declared for the top-level template
//        importChildTemplates(fmConfig, template.childTemplates)

        return fmConfig.getTemplate(templateName)
    }

    private fun newFreeMarkerConfig() = Configuration(Configuration.VERSION_2_3_24).apply {
        defaultEncoding = "UTF-8"
        setClassForTemplateLoading(this.javaClass, "/")
        templateExceptionHandler = TemplateExceptionHandler.DEBUG_HANDLER
    }

}