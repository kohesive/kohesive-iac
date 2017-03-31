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

    fun createDirectory(name: String, init: CreateDirectoryRequest.() -> Unit): CreateDirectoryResult {
        return cloudDirectoryClient.createDirectory(CreateDirectoryRequest().apply {
            withName(name)
            init()
        })
    }

    fun createFacet(name: String, init: CreateFacetRequest.() -> Unit): CreateFacetResult {
        return cloudDirectoryClient.createFacet(CreateFacetRequest().apply {
            withName(name)
            init()
        })
    }

    fun createSchema(name: String, init: CreateSchemaRequest.() -> Unit): CreateSchemaResult {
        return cloudDirectoryClient.createSchema(CreateSchemaRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class CloudDirectoryContext(context: IacContext) : BaseCloudDirectoryContext(context) {

}
