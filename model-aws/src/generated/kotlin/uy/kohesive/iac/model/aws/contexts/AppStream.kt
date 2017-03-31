package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.appstream.AmazonAppStream
import com.amazonaws.services.appstream.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface AppStreamIdentifiable : KohesiveIdentifiable {

}

interface AppStreamEnabled : AppStreamIdentifiable {
    val appStreamClient: AmazonAppStream
    val appStreamContext: AppStreamContext
    fun <T> withAppStreamContext(init: AppStreamContext.(AmazonAppStream) -> T): T = appStreamContext.init(appStreamClient)
}

open class BaseAppStreamContext(protected val context: IacContext) : AppStreamEnabled by context {

    fun createFleet(name: String, init: CreateFleetRequest.() -> Unit): Fleet {
        return appStreamClient.createFleet(CreateFleetRequest().apply {
            withName(name)
            init()
        }).fleet
    }

    fun createStack(name: String, init: CreateStackRequest.() -> Unit): Stack {
        return appStreamClient.createStack(CreateStackRequest().apply {
            withName(name)
            init()
        }).stack
    }


}

@DslScope
class AppStreamContext(context: IacContext) : BaseAppStreamContext(context) {

}
