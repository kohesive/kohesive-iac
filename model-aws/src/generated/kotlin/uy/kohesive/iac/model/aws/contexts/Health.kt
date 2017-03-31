package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.health.AWSHealth
import com.amazonaws.services.health.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface HealthIdentifiable : KohesiveIdentifiable {

}

interface HealthEnabled : HealthIdentifiable {
    val healthClient: AWSHealth
    val healthContext: HealthContext
    fun <T> withHealthContext(init: HealthContext.(AWSHealth) -> T): T = healthContext.init(healthClient)
}

open class BaseHealthContext(protected val context: IacContext) : HealthEnabled by context {


}

@DslScope
class HealthContext(context: IacContext) : BaseHealthContext(context) {

}
