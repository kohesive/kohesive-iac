package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.simpledb.AmazonSimpleDB
import com.amazonaws.services.simpledb.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface SimpleDBIdentifiable : KohesiveIdentifiable {

}

interface SimpleDBEnabled : SimpleDBIdentifiable {
    val simpleDBClient: AmazonSimpleDB
    val simpleDBContext: SimpleDBContext
    fun <T> withSimpleDBContext(init: SimpleDBContext.(AmazonSimpleDB) -> T): T = simpleDBContext.init(simpleDBClient)
}

open class BaseSimpleDBContext(protected val context: IacContext) : SimpleDBEnabled by context {

}

@DslScope
class SimpleDBContext(context: IacContext) : BaseSimpleDBContext(context) {

}