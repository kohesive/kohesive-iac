package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.opsworks.AWSOpsWorks
import com.amazonaws.services.opsworks.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface OpsWorksIdentifiable : KohesiveIdentifiable {

}

interface OpsWorksEnabled : OpsWorksIdentifiable {
    val opsWorksClient: AWSOpsWorks
    val opsWorksContext: OpsWorksContext
    fun <T> withOpsWorksContext(init: OpsWorksContext.(AWSOpsWorks) -> T): T = opsWorksContext.init(opsWorksClient)
}

open class BaseOpsWorksContext(protected val context: IacContext) : OpsWorksEnabled by context {

    fun createApp(name: String, init: CreateAppRequest.() -> Unit): CreateAppResult {
        return opsWorksClient.createApp(CreateAppRequest().apply {
            withName(name)
            init()
        })
    }

    fun createLayer(name: String, init: CreateLayerRequest.() -> Unit): CreateLayerResult {
        return opsWorksClient.createLayer(CreateLayerRequest().apply {
            withName(name)
            init()
        })
    }

    fun createStack(name: String, init: CreateStackRequest.() -> Unit): CreateStackResult {
        return opsWorksClient.createStack(CreateStackRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class OpsWorksContext(context: IacContext) : BaseOpsWorksContext(context) {

}
