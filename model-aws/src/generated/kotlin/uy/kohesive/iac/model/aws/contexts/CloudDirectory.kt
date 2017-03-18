package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.clouddirectory.AmazonCloudDirectory
import com.amazonaws.services.clouddirectory.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CloudDirectoryIdentifiable : KohesiveIdentifiable {

}

interface CloudDirectoryEnabled : CloudDirectoryIdentifiable {
    val cloudDirectoryClient: AmazonCloudDirectory
    val cloudDirectoryContext: CloudDirectoryContext
    fun <T> withCloudDirectoryContext(init: CloudDirectoryContext.(AmazonCloudDirectory) -> T): T = cloudDirectoryContext.init(cloudDirectoryClient)
}

open class BaseCloudDirectoryContext(protected val context: IacContext) : CloudDirectoryEnabled by context {

}

@DslScope
class CloudDirectoryContext(context: IacContext) : BaseCloudDirectoryContext(context) {

}