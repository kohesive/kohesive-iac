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

    fun createComputer(computerName: String, init: CreateComputerRequest.() -> Unit): Computer {
        return directoryServiceClient.createComputer(CreateComputerRequest().apply {
            withComputerName(computerName)
            init()
        }).computer
    }

    fun createDirectory(name: String, init: CreateDirectoryRequest.() -> Unit): CreateDirectoryResult {
        return directoryServiceClient.createDirectory(CreateDirectoryRequest().apply {
            withName(name)
            init()
        })
    }

    fun createMicrosoftAD(name: String, init: CreateMicrosoftADRequest.() -> Unit): CreateMicrosoftADResult {
        return directoryServiceClient.createMicrosoftAD(CreateMicrosoftADRequest().apply {
            withName(name)
            init()
        })
    }

    fun createSnapshot(name: String, init: CreateSnapshotRequest.() -> Unit): CreateSnapshotResult {
        return directoryServiceClient.createSnapshot(CreateSnapshotRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class DirectoryServiceContext(context: IacContext) : BaseDirectoryServiceContext(context) {

}
