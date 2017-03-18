package ${targetClientPackageName}

import ${awsClientPackageName}.Abstract${syncInterface}
import ${awsClientPackageName}.${syncInterface}
import ${awsClientPackageName}.model.*
import uy.kohesive.iac.model.aws.IacContext

open class ${baseDeferredClientClassName}(val context: IacContext) : Abstract${syncInterface}(), ${syncInterface} {

}

class ${deferredClientClassName}(context: IacContext) : ${baseDeferredClientClassName}(context)