package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.redshift.AmazonRedshift
import com.amazonaws.services.redshift.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface RedshiftIdentifiable : KohesiveIdentifiable {

}

interface RedshiftEnabled : RedshiftIdentifiable {
    val redshiftClient: AmazonRedshift
    val redshiftContext: RedshiftContext
    fun <T> withRedshiftContext(init: RedshiftContext.(AmazonRedshift) -> T): T = redshiftContext.init(redshiftClient)
}

open class BaseRedshiftContext(protected val context: IacContext) : RedshiftEnabled by context {

}

@DslScope
class RedshiftContext(context: IacContext) : BaseRedshiftContext(context) {

}