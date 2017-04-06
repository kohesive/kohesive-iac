package uy.kohesive.iac.examples.aws.lambdas

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.model.*
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import freemarker.template.TemplateNotFoundException
import java.io.StringWriter

// TODO: in the real world the templates would be stored somewhere more accessible and editable, S3 or a database
class SesTemplatedEmailService {
    companion object {
        val freemarker = Configuration(Configuration.VERSION_2_3_23).apply {
            setClassLoaderForTemplateLoading(Thread.currentThread().contextClassLoader, "/templates")
            defaultEncoding = "UTF-8"
            templateExceptionHandler = TemplateExceptionHandler.DEBUG_HANDLER
            logTemplateExceptions = false
        }

        fun renderTemplate(templateBaseName: String, renderModel: Any): RenderedTemplate {
            val txtTemplate = try {
                freemarker.getTemplate(templateBaseName + ".txt.ftl")
            } catch (ex: TemplateNotFoundException) {
                null
            }
            val htmlTemplate = try {
                freemarker.getTemplate(templateBaseName + ".html.ftl")
            } catch (ex: TemplateNotFoundException) {
                null
            }
            txtTemplate ?: htmlTemplate ?: throw IllegalStateException("Must have one or both of ${templateBaseName} '.txt.ftl' / '.html.ftl'")
            return RenderedTemplate(txtTemplate?.let { StringWriter().apply { it.process(renderModel, this) }.toString() },
                    htmlTemplate?.let { StringWriter().apply { it.process(renderModel, this) }.toString() })
        }
    }

    fun sendEmail(sesClient: AmazonSimpleEmailService,
                  toName: String?, toAddress: String,
                  senderName: String?, senderAddress: String,
                  subject: String,
                  templateBaseName: String,
                  renderModel: Any): SendEmailResult {
        val toFullAddress = if (toName != null && toName.isNotBlank()) "$toName <$toAddress>" else toAddress
        val fromFullAddress = "$senderName <${senderAddress}>"
        val destination = Destination().withToAddresses(toFullAddress)

        val rendered = renderTemplate(templateBaseName, renderModel)
        val body = Body().apply {
            if (rendered.html != null) {
                withHtml(Content().withData(rendered.html))
            }
            if (rendered.txt != null) {
                withText(Content().withData(rendered.txt))
            }
        }
        val message = Message().withSubject(Content().withData(subject)).withBody(body)
        val sendRequest = SendEmailRequest().withSource(fromFullAddress).withDestination(destination).withMessage(message)

        // TODO: any custom headers for tracking purposes?

        return sesClient.sendEmail(sendRequest)
    }
}

data class RenderedTemplate(val txt: String?, val html: String?)
