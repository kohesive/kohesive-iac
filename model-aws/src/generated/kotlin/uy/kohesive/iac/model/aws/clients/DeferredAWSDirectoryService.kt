package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.directory.AbstractAWSDirectoryService
import com.amazonaws.services.directory.AWSDirectoryService
import com.amazonaws.services.directory.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSDirectoryService(val context: IacContext) : AbstractAWSDirectoryService(), AWSDirectoryService {

}

class DeferredAWSDirectoryService(context: IacContext) : BaseDeferredAWSDirectoryService(context)