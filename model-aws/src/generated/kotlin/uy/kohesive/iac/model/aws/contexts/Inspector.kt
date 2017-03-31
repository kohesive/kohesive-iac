package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.inspector.AmazonInspector
import com.amazonaws.services.inspector.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface InspectorIdentifiable : KohesiveIdentifiable {

}

interface InspectorEnabled : InspectorIdentifiable {
    val inspectorClient: AmazonInspector
    val inspectorContext: InspectorContext
    fun <T> withInspectorContext(init: InspectorContext.(AmazonInspector) -> T): T = inspectorContext.init(inspectorClient)
}

open class BaseInspectorContext(protected val context: IacContext) : InspectorEnabled by context {

    fun createAssessmentTarget(assessmentTargetName: String, init: CreateAssessmentTargetRequest.() -> Unit): CreateAssessmentTargetResult {
        return inspectorClient.createAssessmentTarget(CreateAssessmentTargetRequest().apply {
            withAssessmentTargetName(assessmentTargetName)
            init()
        })
    }

    fun createAssessmentTemplate(assessmentTemplateName: String, init: CreateAssessmentTemplateRequest.() -> Unit): CreateAssessmentTemplateResult {
        return inspectorClient.createAssessmentTemplate(CreateAssessmentTemplateRequest().apply {
            withAssessmentTemplateName(assessmentTemplateName)
            init()
        })
    }


}

@DslScope
class InspectorContext(context: IacContext) : BaseInspectorContext(context) {

}
