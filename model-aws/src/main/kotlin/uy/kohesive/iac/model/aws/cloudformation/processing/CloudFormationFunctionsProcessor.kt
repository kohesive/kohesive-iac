package uy.kohesive.iac.model.aws.cloudformation.processing

import com.fasterxml.jackson.databind.node.ObjectNode
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.cloudformation.Template

class CloudFormationFunctionsProcessor : TemplateStringProcessor {

    override fun scope(templateNode: ObjectNode) =
        templateNode.get(Template::Resources.name) as? ObjectNode

    override fun process(context: IacContext, textNode: TextNodeInParent) {
        // TODO: implement
    }


}