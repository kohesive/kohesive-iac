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

}

@DslScope
class InspectorContext(context: IacContext) : BaseInspectorContext(context) {

}