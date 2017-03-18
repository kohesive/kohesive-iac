package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.directory.AWSDirectoryService
import com.amazonaws.services.directory.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface DirectoryServiceIdentifiable : KohesiveIdentifiable {

}

interface DirectoryServiceEnabled : DirectoryServiceIdentifiable {
    val directoryServiceClient: AWSDirectoryService
    val directoryServiceContext: DirectoryServiceContext
    fun <T> withDirectoryServiceContext(init: DirectoryServiceContext.(AWSDirectoryService) -> T): T = directoryServiceContext.init(directoryServiceClient)
}

open class BaseDirectoryServiceContext(protected val context: IacContext) : DirectoryServiceEnabled by context {

}

@DslScope
class DirectoryServiceContext(context: IacContext) : BaseDirectoryServiceContext(context) {

}