package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.applicationdiscovery.AWSApplicationDiscovery
import com.amazonaws.services.applicationdiscovery.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ApplicationDiscoveryIdentifiable : KohesiveIdentifiable {

}

interface ApplicationDiscoveryEnabled : ApplicationDiscoveryIdentifiable {
    val applicationDiscoveryClient: AWSApplicationDiscovery
    val applicationDiscoveryContext: ApplicationDiscoveryContext
    fun <T> withApplicationDiscoveryContext(init: ApplicationDiscoveryContext.(AWSApplicationDiscovery) -> T): T = applicationDiscoveryContext.init(applicationDiscoveryClient)
}

open class BaseApplicationDiscoveryContext(protected val context: IacContext) : ApplicationDiscoveryEnabled by context {

    fun createApplication(name: String, init: CreateApplicationRequest.() -> Unit): CreateApplicationResult {
        return applicationDiscoveryClient.createApplication(CreateApplicationRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class ApplicationDiscoveryContext(context: IacContext) : BaseApplicationDiscoveryContext(context) {

}
