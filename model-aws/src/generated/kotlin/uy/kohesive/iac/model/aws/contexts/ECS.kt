package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.ecs.AmazonECS
import com.amazonaws.services.ecs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ECSIdentifiable : KohesiveIdentifiable {

}

interface ECSEnabled : ECSIdentifiable {
    val ecsClient: AmazonECS
    val ecsContext: ECSContext
    fun <T> withECSContext(init: ECSContext.(AmazonECS) -> T): T = ecsContext.init(ecsClient)
}

open class BaseECSContext(protected val context: IacContext) : ECSEnabled by context {

}

@DslScope
class ECSContext(context: IacContext) : BaseECSContext(context) {

}