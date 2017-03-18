package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.storagegateway.AbstractAWSStorageGateway
import com.amazonaws.services.storagegateway.AWSStorageGateway
import com.amazonaws.services.storagegateway.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSStorageGateway(val context: IacContext) : AbstractAWSStorageGateway(), AWSStorageGateway {

}

class DeferredAWSStorageGateway(context: IacContext) : BaseDeferredAWSStorageGateway(context)