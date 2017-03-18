package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.kms.AbstractAWSKMS
import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSKMS(val context: IacContext) : AbstractAWSKMS(), AWSKMS {

}

class DeferredAWSKMS(context: IacContext) : BaseDeferredAWSKMS(context)