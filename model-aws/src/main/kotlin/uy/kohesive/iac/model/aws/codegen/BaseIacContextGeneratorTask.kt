package uy.kohesive.iac.model.aws.codegen

import com.amazonaws.codegen.emitters.CodeWriter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import freemarker.template.Template
import java.io.Writer

class BaseIacContextGeneratorTask private constructor(writer: Writer, template: Template, data: Any)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(outputDirectory: String, baseContextData: BaseContextData): BaseIacContextGeneratorTask {

            baseContextData.clientClasses.sort()
            baseContextData.enabledClassNames.sort()

            return BaseIacContextGeneratorTask(
                CodeWriter(
                    outputDirectory + "/" + ContextData.PackagePath,
                    "BaseIacContext",
                    ".kt"
                ),
                TemplateDescriptor.BaseIacContext.load(),
                baseContextData
            )
        }
    }

}

class BaseContextData {
    val contextPackageName: String = ContextData.PackageName
    val clientClasses     = ArrayList<String>()
    val enabledClassNames = ArrayList<String>()
}