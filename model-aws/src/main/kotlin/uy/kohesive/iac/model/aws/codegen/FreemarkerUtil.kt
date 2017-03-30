package uy.kohesive.iac.model.aws.codegen

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler

object TemplateLoader {

    fun getTemplate(freemarkerTemplate: TemplateDescriptor): Template {
        val fmConfig = newFreeMarkerConfig()

        freemarkerTemplate.childTemplates.forEach { (templateLocation, importAsNamespace) ->
            fmConfig.addAutoImport(importAsNamespace, templateLocation)
        }

        return fmConfig.getTemplate(freemarkerTemplate.location)
    }

    private fun newFreeMarkerConfig() = Configuration(Configuration.VERSION_2_3_24).apply {
        defaultEncoding = "UTF-8"
        setClassForTemplateLoading(this.javaClass, "/")
        templateExceptionHandler = TemplateExceptionHandler.DEBUG_HANDLER
    }

}