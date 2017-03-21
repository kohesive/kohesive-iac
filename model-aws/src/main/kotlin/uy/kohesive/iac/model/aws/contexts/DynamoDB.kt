package uy.kohesive.iac.model.aws.contexts

import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.utils.DslScope

@DslScope
class DynamoDBContext(context: IacContext) : BaseDynamoDBContext(context) {

}