package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.workdocs.AmazonWorkDocs
import com.amazonaws.services.workdocs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface WorkDocsIdentifiable : KohesiveIdentifiable {

}

interface WorkDocsEnabled : WorkDocsIdentifiable {
    val workDocsClient: AmazonWorkDocs
    val workDocsContext: WorkDocsContext
    fun <T> withWorkDocsContext(init: WorkDocsContext.(AmazonWorkDocs) -> T): T = workDocsContext.init(workDocsClient)
}

open class BaseWorkDocsContext(protected val context: IacContext) : WorkDocsEnabled by context {

    open fun createFolder(name: String, init: CreateFolderRequest.() -> Unit): CreateFolderResult {
        return workDocsClient.createFolder(CreateFolderRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class WorkDocsContext(context: IacContext) : BaseWorkDocsContext(context) {

}
