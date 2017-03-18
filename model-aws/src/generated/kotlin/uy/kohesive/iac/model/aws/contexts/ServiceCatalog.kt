package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.servicecatalog.AWSServiceCatalog
import com.amazonaws.services.servicecatalog.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ServiceCatalogIdentifiable : KohesiveIdentifiable {

}

interface ServiceCatalogEnabled : ServiceCatalogIdentifiable {
    val serviceCatalogClient: AWSServiceCatalog
    val serviceCatalogContext: ServiceCatalogContext
    fun <T> withServiceCatalogContext(init: ServiceCatalogContext.(AWSServiceCatalog) -> T): T = serviceCatalogContext.init(serviceCatalogClient)
}

open class BaseServiceCatalogContext(protected val context: IacContext) : ServiceCatalogEnabled by context {

}

@DslScope
class ServiceCatalogContext(context: IacContext) : BaseServiceCatalogContext(context) {

}