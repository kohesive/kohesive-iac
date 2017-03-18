package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cloudsearchv2.AmazonCloudSearch
import com.amazonaws.services.cloudsearchv2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CloudSearchIdentifiable : KohesiveIdentifiable {

}

interface CloudSearchEnabled : CloudSearchIdentifiable {
    val cloudSearchClient: AmazonCloudSearch
    val cloudSearchContext: CloudSearchContext
    fun <T> withCloudSearchContext(init: CloudSearchContext.(AmazonCloudSearch) -> T): T = cloudSearchContext.init(cloudSearchClient)
}

open class BaseCloudSearchContext(protected val context: IacContext) : CloudSearchEnabled by context {

}

@DslScope
class CloudSearchContext(context: IacContext) : BaseCloudSearchContext(context) {

}