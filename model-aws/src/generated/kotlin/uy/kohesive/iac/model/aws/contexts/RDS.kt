package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.rds.AmazonRDS
import com.amazonaws.services.rds.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface RDSIdentifiable : KohesiveIdentifiable {

}

interface RDSEnabled : RDSIdentifiable {
    val rdsClient: AmazonRDS
    val rdsContext: RDSContext
    fun <T> withRDSContext(init: RDSContext.(AmazonRDS) -> T): T = rdsContext.init(rdsClient)
}

open class BaseRDSContext(protected val context: IacContext) : RDSEnabled by context {

}

@DslScope
class RDSContext(context: IacContext) : BaseRDSContext(context) {

}