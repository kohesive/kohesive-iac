package ${contextPackageName}

import ${metadata.packageName}.${syncInterface}
import ${metadata.packageName}.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ${serviceName}Identifiable : KohesiveIdentifiable {

}

interface ${serviceName}Enabled : ${serviceName}Identifiable {
    val ${serviceNameLC}Client: ${syncInterface}
    val ${serviceNameLC}Context: ${serviceName}
    fun <T> with${serviceName}Context(init: ${serviceName}Context.(${syncInterface}) -> T): T = ${serviceNameLC}Context.init(${serviceNameLC}Client)
}

@DslScope
class ${serviceName}Context(private val context: IacContext) : ${serviceName}Enabled by context {

}