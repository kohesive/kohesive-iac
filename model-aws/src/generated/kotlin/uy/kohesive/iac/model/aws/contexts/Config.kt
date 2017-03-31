package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.config.AmazonConfig
import com.amazonaws.services.config.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ConfigIdentifiable : KohesiveIdentifiable {

}

interface ConfigEnabled : ConfigIdentifiable {
    val configClient: AmazonConfig
    val configContext: ConfigContext
    fun <T> withConfigContext(init: ConfigContext.(AmazonConfig) -> T): T = configContext.init(configClient)
}

open class BaseConfigContext(protected val context: IacContext) : ConfigEnabled by context {


}

@DslScope
class ConfigContext(context: IacContext) : BaseConfigContext(context) {

}
