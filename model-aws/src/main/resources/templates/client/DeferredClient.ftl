package ${targetClientPackageName}

import ${awsClientPackageName}.Abstract${syncInterface}
import ${awsClientPackageName}.${syncInterface}
import ${awsClientPackageName}.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferred${syncInterface}(val context: IacContext) : Abstract${syncInterface}(), ${syncInterface} {

}

class Deferred${syncInterface}(context: IacContext) : BaseDeferred${syncInterface}(context)